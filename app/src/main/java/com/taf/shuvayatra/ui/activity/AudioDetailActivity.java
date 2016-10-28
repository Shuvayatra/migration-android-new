package com.taf.shuvayatra.ui.activity;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.taf.data.utils.Logger;
import com.taf.model.BaseModel;
import com.taf.model.Post;
import com.taf.shuvayatra.R;
import com.taf.shuvayatra.databinding.ActivityAudioDetailBinding;
import com.taf.shuvayatra.media.MediaHelper;
import com.taf.shuvayatra.media.MediaReceiver;
import com.taf.shuvayatra.media.MediaService;
import com.taf.shuvayatra.ui.interfaces.ListItemClickListener;
import com.taf.shuvayatra.ui.views.AudioDetailView;
import com.taf.shuvayatra.ui.views.AudioPlayerView;
import com.taf.util.MyConstants;

import java.io.IOException;

import butterknife.BindView;

public class AudioDetailActivity extends PostDetailActivity implements
        AudioDetailView,
        AudioPlayerView,
        View.OnClickListener,
        ListItemClickListener,
        SeekBar.OnSeekBarChangeListener {

    private static final int MY_PERMISSIONS_REQUEST_WRITE_STORAGE = 123;
    private static final int GROUP1 = 101;
    private static final int SUBMENU_BLUETOOTH = 1001;
    private static final int SUBMENU_FACEBOOK = 1002;
    private static final String KEY_PROGRESS = "key_progress";

    @BindView(R.id.scroll_view)
    NestedScrollView mScrollView;
    @BindView(R.id.audio_time)
    TextView mAudioTime;
    @BindView(R.id.audio_time_mini)
    TextView mAudioTimeMini;
    @BindView(R.id.play)
    ImageView mPlayBtn;
    @BindView(R.id.play_mini)
    ImageView mPlayBtnMini;
    @BindView(R.id.seekbar)
    SeekBar mSeekbar;
    @BindView(R.id.seekbar_mini)
    SeekBar mSeekbarMini;
    @BindView(R.id.mini_player)
    LinearLayout mMiniPlayer;
    @BindView(R.id.app_bar)
    AppBarLayout mAppBar;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout mCollapsingToolbar;
    @BindView(R.id.buffering)
    TextView bufferingText;
    @BindView(R.id.buffering_mini)
    TextView bufferingTextMini;

    @BindView(R.id.overlay)
    RelativeLayout mOverlay;

    private MediaService mService;
    private Intent mPlayIntent;
    private boolean mMusicBound = false;

    private Handler seekbarHandler = new Handler();
    private MediaReceiver mediaReceiver;
    private IntentFilter receiverFilter;

    private int mCurrentProgress;

    //connect to the service
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MediaService.MusicBinder binder = (MediaService.MusicBinder) service;
            mService = binder.getService();
            mService.setTrack(AudioDetailActivity.this.mPost);
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
                    mCurrentProgress = 0;
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
    protected void onDestroy() {
        mService.stopPlayback();
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
    public void onPause() {
        mSeekbar.removeCallbacks(updateSeekTime);
        mSeekbarMini.removeCallbacks(updateSeekTime);
        if (mediaReceiver != null)
            unregisterReceiver(mediaReceiver);
        super.onPause();
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
    protected void onStop() {
        super.onStop();
    }

    private void shareViaBluetooth() {
//        AnalyticsUtil.logBluetoothShareEvent(getAnalytics(), mPost.getId(), mPost.getTitle(),
//                mPost.getType());
        mPresenter.shareViaBluetooth(mPost);
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
        mCurrentProgress = seekBar.getProgress();
        mService.seekTo(seekBar.getProgress());
        updateSeekBar();
    }

    @Override
    public void onListItemSelected(BaseModel pModel, int pIndex) {
        mId = pModel.getId();
        initialize();

        loadPost();
    }

    @Override
    public void showLoadingView() {
        mOverlay.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoadingView() {
        mOverlay.setVisibility(View.GONE);
    }

    @Override
    public void onMediaPlayPause() {
        mService.playPause();
    }

    @Override
    public void onBufferStarted() {
        bufferingText.setVisibility(View.VISIBLE);
        bufferingTextMini.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBufferStopped() {
        bufferingText.setVisibility(View.GONE);
        bufferingTextMini.setVisibility(View.GONE);
    }

    @Override
    public void onMediaPrepared() {
        updateSeekBar();
        Logger.d("AudioDetailActivity_onMediaPrepared", "test: " + mCurrentProgress);
        mService.seekTo(mCurrentProgress);
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
    public void onAudioDownloadStarted(String pMessage) {
        Snackbar.make(mMiniPlayer, pMessage, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onAudioFileNotFoundToShare() {
        Snackbar.make(mMiniPlayer, getString(R.string.error_bluetooth_share), Snackbar
                .LENGTH_SHORT).show();
    }

    @Override
    public void updateView(Post post) {
        if (mPlayIntent == null) {
            mPlayIntent = new Intent(this, MediaService.class);
            bindService(mPlayIntent, mConnection, Context.BIND_AUTO_CREATE);
            startService(mPlayIntent);
        } else {
            mSeekbar.removeCallbacks(updateSeekTime);
            mService.stopPlayback();
            mService.setTrack(post);
            mService.startStreaming();
        }
        mScrollView.scrollTo(0, 0);
        mAppBar.setExpanded(true);
        ((ActivityAudioDetailBinding) mBinding).setAudio(post);
        ((ActivityAudioDetailBinding) mBinding).setSimilarAudios(post.getSimilarPosts());
        invalidateOptionsMenu();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getToolbar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState != null) {
            mCurrentProgress = savedInstanceState.getInt(KEY_PROGRESS, 0);
        }

        ((ActivityAudioDetailBinding) mBinding).setListener(this);
        initialize();

        mediaReceiver = new MediaReceiver(this);
        receiverFilter = new IntentFilter();

        mPlayBtn.setOnClickListener(this);
        mPlayBtnMini.setOnClickListener(this);
        mSeekbar.setOnSeekBarChangeListener(this);
        mSeekbarMini.setOnSeekBarChangeListener(this);

        mAppBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (mCollapsingToolbar.getHeight() + verticalOffset < 2 * ViewCompat
                        .getMinimumHeight(mCollapsingToolbar)) {
                    mMiniPlayer.animate().alpha(1).setDuration(300);
                } else {
                    mMiniPlayer.animate().alpha(0).setDuration(300);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.audio_detail, menu);

        menu.findItem(R.id.action_share).getSubMenu().add(GROUP1, SUBMENU_BLUETOOTH, 1,
                getString(R.string.action_share_bluetooth));
        menu.findItem(R.id.action_share).getSubMenu().add(GROUP1, SUBMENU_FACEBOOK, 2, getString
                (R.string.action_share_facebook));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.action_download:
                try {
                    if (!getPreferences().getDownloadReferences().contains(mPost
                            .getDownloadReference())) {
                        /*AnalyticsUtil.logDownloadEvent(getAnalytics(), mAudio.getId(), mAudio
                                .getTitle(), mAudio.getType());*/
                        requestForPermissions();
                    }
                } catch (IOException e) {
                    Logger.e("AudioDetailActivity_onOptionsItemSelected", "errorMessage: " + e
                            .getLocalizedMessage());
                }
                break;
            case SUBMENU_BLUETOOTH:
                shareViaBluetooth();
                break;
            case SUBMENU_FACEBOOK:
                share(mPost);
                break;

        }
        return true;
    }

    @Override
    protected void initialize() {
        super.initialize();
        mPresenter.attachView(this);
        mPresenter.initialize(null);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_STORAGE:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager
                        .PERMISSION_GRANTED) {
                    try {
                        mPresenter.downloadAudioPost(mPost);
                    } catch (IOException e) {
                        Snackbar.make(mBinding.getRoot(), "Could not download the audio. Try " +
                                "again later.", Snackbar.LENGTH_LONG).show();
                    }
                }
                return;
        }
    }

    private void updateSeekBar() {
        seekbarHandler.removeCallbacks(updateSeekTime);
        seekbarHandler.postDelayed(updateSeekTime, 100);
    }

    public void requestForPermissions() throws IOException {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission
                .WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (this.shouldShowRequestPermissionRationale(Manifest.permission
                    .WRITE_EXTERNAL_STORAGE)) {
                Snackbar.make(mBinding.getRoot(), "This app requires permission to download audio" +
                        " to the device.", Snackbar.LENGTH_LONG).show();
            }
            this.requestPermissions(new String[]{Manifest.permission
                    .READ_CALENDAR}, MY_PERMISSIONS_REQUEST_WRITE_STORAGE);
        } else {
            mPresenter.downloadAudioPost(mPost);
        }
    }
}
