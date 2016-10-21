package com.taf.shuvayatra.ui.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.taf.data.utils.Logger;
import com.taf.model.BaseModel;
import com.taf.model.Podcast;
import com.taf.shuvayatra.R;
import com.taf.shuvayatra.base.BaseActivity;
import com.taf.shuvayatra.databinding.PodcastPlayerDataBinding;
import com.taf.shuvayatra.di.component.DaggerDataComponent;
import com.taf.shuvayatra.di.module.DataModule;
import com.taf.shuvayatra.media.MediaHelper;
import com.taf.shuvayatra.media.MediaReceiver;
import com.taf.shuvayatra.media.MediaService;
import com.taf.shuvayatra.presenter.PodcastListPresenter;
import com.taf.shuvayatra.ui.adapter.ListAdapter;
import com.taf.shuvayatra.ui.interfaces.ListItemClickListener;
import com.taf.shuvayatra.ui.views.AudioPlayerView;
import com.taf.shuvayatra.ui.views.PodcastListView;
import com.taf.util.MyConstants;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class PodcastsActivity extends BaseActivity implements
        PodcastListView,
        AudioPlayerView,
        View.OnClickListener,
        ListItemClickListener,
        SeekBar.OnSeekBarChangeListener,
        SwipeRefreshLayout.OnRefreshListener {

    @Inject
    PodcastListPresenter mPresenter;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipe_container)
    SwipeRefreshLayout mSwipeContainer;

    @BindView(R.id.player)
    LinearLayout mPlayer;
    @BindView(R.id.audio_time)
    TextView mAudioTime;
    @BindView(R.id.play)
    ImageView mPlayBtn;
    @BindView(R.id.seekbar)
    SeekBar mSeekbar;
    @BindView(R.id.buffering)
    TextView bufferingText;

    ListAdapter<Podcast> mAdapter;

    private MediaService mService;
    private Intent mPlayIntent;
    private boolean mMusicBound = false;

    private Handler seekbarHandler = new Handler();
    private MediaReceiver mediaReceiver;
    private IntentFilter receiverFilter;

    private int mCurrentProgress;

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Logger.d("PodcastsActivity_onServiceConnected", "test: " + mAdapter.getItemCount());
            MediaService.MusicBinder binder = (MediaService.MusicBinder) service;
            mService = binder.getService();
            mService.setPodcasts(mAdapter.getDataCollection());
            mService.startStreaming();
            mMusicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Logger.d("PodcastsActivity_onServiceDisconnected", "test");
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

                    int progress = MediaHelper.getProgressPercentage(lengths[0], lengths[1]);
                    mSeekbar.setProgress(progress);
                    seekbarHandler.postDelayed(this, 500);
                } else {
                    mCurrentProgress = 0;
                    mSeekbar.setProgress(0);
                    mSeekbar.removeCallbacks(updateSeekTime);
                }
            }
        }
    };

    @Override
    public int getLayout() {
        return R.layout.activity_podcasts;
    }

    @Override
    public boolean isDataBindingEnabled() {
        return true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initialize();

        mAdapter = new ListAdapter(getContext(), this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
        mSwipeContainer.setOnRefreshListener(this);

        mediaReceiver = new MediaReceiver(this);
        receiverFilter = new IntentFilter();

        mPlayBtn.setOnClickListener(this);
        mSeekbar.setOnSeekBarChangeListener(this);

        mPresenter.initialize(null);
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

            updateView(mService.getCurrentPodcast());
            onBufferStopped();
        }
        receiverFilter.addAction(MyConstants.Media.ACTION_MEDIA_BUFFER_START);
        receiverFilter.addAction(MyConstants.Media.ACTION_MEDIA_BUFFER_STOP);
        receiverFilter.addAction(MyConstants.Media.ACTION_STATUS_PREPARED);
        receiverFilter.addAction(MyConstants.Media.ACTION_PLAY_STATUS_CHANGE);
        receiverFilter.addAction(MyConstants.Media.ACTION_MEDIA_COMPLETE);
        registerReceiver(mediaReceiver, receiverFilter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListItemSelected(BaseModel pModel, int pIndex) {

    }

    @Override
    public void onRefresh() {
        mPresenter.initialize(null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.play:
                updateSeekBar();
                onMediaPlayPause();
                break;
            default:
                break;
        }
    }

    @Override
    public void onMediaPlayPause() {
        mService.playPause();
    }

    @Override
    public void onBufferStarted() {
        bufferingText.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBufferStopped() {
        bufferingText.setVisibility(View.GONE);
    }

    @Override
    public void onMediaPrepared() {
        updateSeekBar();
        mService.seekTo(mCurrentProgress);
        mPlayBtn.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause));
        updateView(mService.getCurrentPodcast());
    }

    @Override
    public void onMediaComplete() {
        mCurrentProgress = 0;
        mPlayBtn.setImageDrawable(getResources().getDrawable(R.drawable.ic_play));
    }

    @Override
    public void playStatusChanged(boolean pIsPlaying) {
        if (pIsPlaying) {
            mPlayBtn.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause));
        } else {
            mPlayBtn.setImageDrawable(getResources().getDrawable(R.drawable.ic_play));
        }
    }

    private void updateView(Podcast podcast) {
        ((PodcastPlayerDataBinding) mBinding).setPodcast(podcast);
    }

    private void updateSeekBar() {
        seekbarHandler.removeCallbacks(updateSeekTime);
        seekbarHandler.postDelayed(updateSeekTime, 100);
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
    public void renderPodcasts(List<Podcast> podcasts) {
        mAdapter.setDataCollection(podcasts);
        if (mPlayIntent == null) {
            mPlayIntent = new Intent(this, MediaService.class);
            bindService(mPlayIntent, mConnection, Context.BIND_AUTO_CREATE);
            startService(mPlayIntent);
        } else {
            Logger.d("PodcastsActivity_renderPodcasts", "test");
            mSeekbar.removeCallbacks(updateSeekTime);
            mService.stopPlayback();
            mService.setPodcasts(podcasts);
            mService.startStreaming();
        }
        mPlayer.setVisibility(podcasts.isEmpty() ? View.GONE : View.VISIBLE);
    }

    @Override
    public void showLoadingView() {
        mSwipeContainer.setRefreshing(true);
    }

    @Override
    public void hideLoadingView() {
        mSwipeContainer.setRefreshing(false);
    }

    @Override
    public void showErrorView(String errorMessage) {
        Snackbar.make(mSwipeContainer, errorMessage, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public Context getContext() {
        return this;
    }

    private void initialize() {
        DaggerDataComponent.builder()
                .activityModule(getActivityModule())
                .applicationComponent(getApplicationComponent())
                .dataModule(new DataModule(1L)) //// TODO: 10/24/16 get radio-channel id
                .build()
                .inject(this);

        mPresenter.attachView(this);
    }
}
