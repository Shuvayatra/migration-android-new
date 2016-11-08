package com.taf.shuvayatra.ui.fragment;


import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.taf.shuvayatra.MyApplication;
import com.taf.shuvayatra.R;
import com.taf.shuvayatra.base.BaseFragment;
import com.taf.shuvayatra.media.MediaHelper;
import com.taf.shuvayatra.media.MediaReceiver;
import com.taf.shuvayatra.ui.interfaces.PlayerFragmentCallback;
import com.taf.shuvayatra.ui.views.AudioPlayerView;
import com.taf.util.MyConstants;

import butterknife.BindView;
import butterknife.OnClick;

public class MiniPlayerFragment extends BaseFragment implements
        AudioPlayerView,
        View.OnClickListener,
        SeekBar.OnSeekBarChangeListener {

    public static final String TAG = "MiniPlayerFragment";
    private static int mCurrentProgress;
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.audio_time)
    TextView mAudioTime;
    @BindView(R.id.play)
    ImageView mPlayBtn;
    @BindView(R.id.seekbar)
    SeekBar mSeekbar;
    @BindView(R.id.buffering)
    TextView mBufferingText;
    private MediaReceiver mediaReceiver;
    private IntentFilter receiverFilter;
    private boolean seekbarChangeByUser = false;

    private PlayerFragmentCallback mCallback;

    public MiniPlayerFragment() {
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_mini_player;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mediaReceiver = new MediaReceiver(this);
        receiverFilter = new IntentFilter();

        mPlayBtn.setOnClickListener(this);
        mSeekbar.setOnSeekBarChangeListener(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallback = (PlayerFragmentCallback) context;
    }

    @Override
    public void onResume() {
        super.onResume();
        receiverFilter.addAction(MyConstants.Media.ACTION_MEDIA_BUFFER_START);
        receiverFilter.addAction(MyConstants.Media.ACTION_MEDIA_BUFFER_STOP);
        receiverFilter.addAction(MyConstants.Media.ACTION_STATUS_PREPARED);
        receiverFilter.addAction(MyConstants.Media.ACTION_PLAY_STATUS_CHANGE);
        receiverFilter.addAction(MyConstants.Media.ACTION_MEDIA_PLAYBACK_CHANGE);
        receiverFilter.addAction(MyConstants.Media.ACTION_PROGRESS_CHANGE);
        getContext().registerReceiver(mediaReceiver, receiverFilter);

        updateView();
    }

    @Override
    public void onPause() {
        if (mediaReceiver != null)
            getContext().unregisterReceiver(mediaReceiver);
        super.onPause();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.play:
                ((MyApplication) getContext().getApplicationContext()).mService.playPause();
                break;
            default:
                break;
        }
    }

    @OnClick(R.id.close)
    public void closePlayer() {
        mCallback.removePlayer();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        seekbarChangeByUser = true;
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mCurrentProgress = seekBar.getProgress();
        ((MyApplication) getContext().getApplicationContext()).mService.seekTo(mCurrentProgress);
        seekbarChangeByUser = false;
    }

    @Override
    public void onBufferStarted() {
        mBufferingText.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBufferStopped() {
        mBufferingText.setVisibility(View.GONE);
    }

    @Override
    public void onMediaPrepared() {
        ((MyApplication) getContext().getApplicationContext()).mService.seekTo(mCurrentProgress);
        mPlayBtn.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause));

        updateView();
    }

    @Override
    public void onMediaComplete() {
        mPlayBtn.setImageDrawable(getResources().getDrawable(R.drawable.ic_play));
    }

    @Override
    public void playStatusChanged(boolean isPlaying) {
        mPlayBtn.setImageDrawable(getResources().getDrawable(isPlaying ? R.drawable.ic_pause : R
                .drawable.ic_play));
    }

    public void onMediaProgressReset() {
        if (!seekbarChangeByUser) {
            mCurrentProgress = 0;
            mSeekbar.setProgress(0);
        }
    }

    @Override
    public void onMediaProgressChanged(long[] lengths) {
        if (!seekbarChangeByUser) {
            mAudioTime.setText(getString(R.string.audio_time, MediaHelper
                    .getFormattedTime(lengths[0]), MediaHelper.getFormattedTime
                    (lengths[1])));

            int progress = MediaHelper.getProgressPercentage(lengths[0], lengths[1]);
            mSeekbar.setProgress(progress);
        }
    }

    private void updateView() {
        mPlayBtn.setImageDrawable(getResources().getDrawable(
                ((MyApplication) getContext().getApplicationContext()).mService.getPlayStatus() ?
                        R.drawable.ic_pause : R.drawable.ic_play)
        );
        mTitle.setText(((MyApplication) getContext().getApplicationContext()).mService
                .getCurrentTitle());
    }
}
