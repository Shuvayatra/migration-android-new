package com.taf.shuvayatra.ui.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.taf.data.utils.Logger;
import com.taf.model.Post;
import com.taf.shuvayatra.R;
import com.taf.shuvayatra.base.BaseActivity;
import com.taf.shuvayatra.databinding.ActivityAudioDetailBinding;
import com.taf.shuvayatra.media.MediaHelper;
import com.taf.shuvayatra.media.MediaReceiver;
import com.taf.shuvayatra.media.MediaService;
import com.taf.shuvayatra.ui.interfaces.AudioDetailView;
import com.taf.util.MyConstants;

import butterknife.Bind;

public class AudioDetailActivity extends BaseActivity implements
        AudioDetailView,
        View.OnClickListener,
        SeekBar.OnSeekBarChangeListener {

    @Bind(R.id.audio_time)
    TextView mAudioTime;
    @Bind(R.id.audio_time_mini)
    TextView mAudioTimeMini;
    @Bind(R.id.play)
    ImageView mPlayBtn;
    @Bind(R.id.play_mini)
    ImageView mPlayBtnMini;
    @Bind(R.id.seekbar)
    SeekBar mSeekbar;
    @Bind(R.id.seekbar_mini)
    SeekBar mSeekbarMini;
    @Bind(R.id.mini_player)
    LinearLayout mMiniPlayer;
    @Bind(R.id.app_bar)
    AppBarLayout mAppBar;
    @Bind(R.id.collapsing_toolbar)
    CollapsingToolbarLayout mCollapsingToolbar;

    private MediaService mService;
    private Intent mPlayIntent;
    private boolean mMusicBound = false;

    private Handler seekbarHandler = new Handler();
    private MediaReceiver mediaReceiver;
    private IntentFilter receiverFilter;

    private Post mAudio;

    //connect to the service
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MediaService.MusicBinder binder = (MediaService.MusicBinder) service;
            mService = binder.getService();
            mService.setTrack(mAudio);
            mService.startStreaming();
            mMusicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mMusicBound = false;
        }
    };

    private Runnable updateSeekTime = new Runnable() {
        @Override
        public void run() {
            if (mService != null) {
                long[] lengths = mService.getSongLengths();
                if (lengths != null) {
                    mAudioTime.setText(getString(R.string.audio_time, MediaHelper
                            .getFormattedTime(lengths[0]), MediaHelper.getFormattedTime
                            (lengths[1])));
                    mAudioTimeMini.setText(getString(R.string.audio_time, MediaHelper
                            .getFormattedTime(lengths[0]), MediaHelper.getFormattedTime
                            (lengths[1])));

                    int progress = MediaHelper.getProgressPercentage(lengths[0], lengths[1]);
                    mSeekbar.setProgress(progress);
                    mSeekbarMini.setProgress(progress);
                    seekbarHandler.postDelayed(this, 500);
                } else {
                    mSeekbar.setProgress(0);
                    mSeekbar.removeCallbacks(updateSeekTime);
                    mSeekbarMini.setProgress(0);
                    mSeekbarMini.removeCallbacks(updateSeekTime);
                }
            }
        }
    };

    @Override
    public int getLayout() {
        return R.layout.activity_audio_detail;
    }

    @Override
    public boolean isDataBindingEnabled() {
        return true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle data = getIntent().getExtras();
        if (data != null) {
            mAudio = (Post) data.getSerializable(MyConstants.Extras.KEY_AUDIO);
            updateView(mAudio);
        }

        getToolbar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mediaReceiver = new MediaReceiver(this);
        receiverFilter = new IntentFilter();

        mPlayBtn.setOnClickListener(this);
        mPlayBtnMini.setOnClickListener(this);
        mSeekbar.setOnSeekBarChangeListener(this);
        mSeekbarMini.setOnSeekBarChangeListener(this);

        mAppBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if(mCollapsingToolbar.getHeight() + verticalOffset < 2 * ViewCompat
                        .getMinimumHeight(mCollapsingToolbar)) {
                    mMiniPlayer.animate().alpha(1).setDuration(300);
                } else {
                    mMiniPlayer.animate().alpha(0).setDuration(300);
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        updateSeekBar();
        if (mMusicBound) {
            mPlayBtn.setImageDrawable(getResources().getDrawable((mService.isMediaValid() &&
                    mService.getPlayStatus()) ? R.drawable.ic_pause : R.drawable.ic_play));

            updateView(mService.getCurrentTrack());
            onBufferStopped();
        }
        receiverFilter.addAction(MyConstants.Media.ACTION_MEDIA_BUFFER_START);
        receiverFilter.addAction(MyConstants.Media.ACTION_MEDIA_BUFFER_STOP);
        receiverFilter.addAction(MyConstants.Media.ACTION_STATUS_PREPARED);
        receiverFilter.addAction(MyConstants.Media.ACTION_PLAY_STATUS_CHANGE);
        registerReceiver(mediaReceiver, receiverFilter);
    }

    @Override
    protected void onDestroy() {
        stopService(mPlayIntent);
        // Unbind from the service
        if (mMusicBound) {
            unbindService(mConnection);
            mMusicBound = false;
        }
        mService = null;
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.audio_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_download:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onPause() {
        mSeekbar.removeCallbacks(updateSeekTime);
        mSeekbarMini.removeCallbacks(updateSeekTime);
        if (mediaReceiver != null)
            unregisterReceiver(mediaReceiver);
        super.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mPlayIntent == null) {
            mPlayIntent = new Intent(this, MediaService.class);
            bindService(mPlayIntent, mConnection, Context.BIND_AUTO_CREATE);
            startService(mPlayIntent);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.play:
            case R.id.play_mini:
                updateSeekBar();
                onMediaPlayPause();
                break;
            default:
                break;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        seekbarHandler.removeCallbacks(updateSeekTime);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mService.seekTo(seekBar.getProgress());
        updateSeekBar();
    }

    @Override
    public void onMediaPlayPause() {
        mService.playPause();
    }

    @Override
    public void onSeekTo(int progress) {
        mService.seekTo(progress);
    }

    @Override
    public void onBufferStarted() {
        // TODO: 4/19/16  
        Logger.d("AudioDetailActivity_onBufferStarted", "buffering start");
    }

    @Override
    public void onBufferStopped() {
        // TODO: 4/19/16
        Logger.d("AudioDetailActivity_onBufferStopped", "buffering stop");
    }

    @Override
    public void onMediaPrepared() {
        updateSeekBar();
        mPlayBtn.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause));
        mPlayBtnMini.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause));
    }

    @Override
    public void onMediaComplete() {
        mPlayBtn.setImageDrawable(getResources().getDrawable(R.drawable.ic_play));
        mPlayBtnMini.setImageDrawable(getResources().getDrawable(R.drawable.ic_play));
    }

    @Override
    public void playStatusChanged(boolean pIsPlaying) {
        if (pIsPlaying) {
            mPlayBtn.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause));
            mPlayBtnMini.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause));
        } else {
            mPlayBtn.setImageDrawable(getResources().getDrawable(R.drawable.ic_play));
            mPlayBtnMini.setImageDrawable(getResources().getDrawable(R.drawable.ic_play));
        }
    }

    @Override
    public Context getContext() {
        return this;
    }

    private void updateView(Post pAudio) {
        ((ActivityAudioDetailBinding) mBinding).setAudio(pAudio);
    }

    private void updateSeekBar() {
        seekbarHandler.removeCallbacks(updateSeekTime);
        seekbarHandler.postDelayed(updateSeekTime, 100);
    }
}
