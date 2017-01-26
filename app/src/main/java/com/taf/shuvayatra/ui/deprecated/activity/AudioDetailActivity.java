package com.taf.shuvayatra.ui.deprecated.activity;

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
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.taf.data.utils.Logger;
import com.taf.interactor.UseCaseData;
import com.taf.model.BaseModel;
import com.taf.model.Post;
import com.taf.shuvayatra.R;
import com.taf.shuvayatra.base.FacebookActivity;
import com.taf.shuvayatra.databinding.ActivityAudioDetailBinding;
import com.taf.shuvayatra.di.component.DaggerDataComponent;
import com.taf.shuvayatra.di.module.DataModule;
import com.taf.shuvayatra.media.MediaHelper;
import com.taf.shuvayatra.media.MediaReceiver;
import com.taf.shuvayatra.media.MediaService;
import com.taf.shuvayatra.presenter.AudioOperationsPresenter;
import com.taf.shuvayatra.presenter.deprecated.PostFavouritePresenter;
import com.taf.shuvayatra.presenter.deprecated.PostViewCountPresenter;
import com.taf.shuvayatra.presenter.deprecated.SimilarPostPresenter;
import com.taf.shuvayatra.ui.deprecated.interfaces.PostListView;
import com.taf.shuvayatra.ui.interfaces.ListItemClickListener;
import com.taf.shuvayatra.ui.views.AudioDetailView;
import com.taf.shuvayatra.ui.views.AudioPlayerView;
import com.taf.shuvayatra.util.AnalyticsUtil;
import com.taf.util.MyConstants;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
@Deprecated
public class AudioDetailActivity extends FacebookActivity implements
        AudioDetailView,
        AudioPlayerView,
        PostListView,
        View.OnClickListener,
        ListItemClickListener,
        SeekBar.OnSeekBarChangeListener {

    private static final int GROUP1 = 101;
    private static final int SUBMENU_BLUETOOTH = 1001;
    private static final int SUBMENU_FACEBOOK = 1002;
    private static final String KEY_AUDIO = "key_audio";
    private static final String KEY_PROGRESS = "key_progress";

    @Inject
    AudioOperationsPresenter mPresenter;
    @Inject
    PostFavouritePresenter mFavouritePresenter;
    @Inject
    PostViewCountPresenter mPostViewCountPresenter;
    @Inject
    SimilarPostPresenter mSimilarPresenter;

    @BindView(R.id.scroll_view)
    NestedScrollView mScrollView;
    @BindView(R.id.audio_time)
    TextView mAudioTime;
    @BindView(R.id.play)
    ImageView mPlayBtn;
    @BindView(R.id.seekbar)
    SeekBar mSeekbar;
    @BindView(R.id.app_bar)
    AppBarLayout mAppBar;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout mCollapsingToolbar;
    @BindView(R.id.buffering)
    TextView bufferingText;

    private MediaService mService;
    private Intent mPlayIntent;
    private boolean mMusicBound = false;

    private Handler seekbarHandler = new Handler();
    private MediaReceiver mediaReceiver;
    private IntentFilter receiverFilter;

    private Post mAudio;
    private int mCurrentProgress;
    private boolean mOldFavouriteState;

    //connect to the service
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MediaService.MusicBinder binder = (MediaService.MusicBinder) service;
            mService = binder.getService();
            mService.setTrack(mAudio);
            mService.startStreaming(true);
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
        return R.layout.activity_audio_detail;
    }

    @Override
    public boolean isDataBindingEnabled() {
        return true;
    }

    @Override
    public boolean containsShareOption() {
        return true;
    }

    @Override
    public boolean containsFavouriteOption() {
        return true;
    }

    @Override
    protected void onDestroy() {
        mService.stopPlayback(false);
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
            case android.R.id.home:
                finishWithResult();
                break;
            case R.id.action_download:
                try {
                    if (!getPreferences().getDownloadReferences().contains(mAudio
                            .getDownloadReference())) {
                        AnalyticsUtil.logDownloadEvent(getAnalytics(), mAudio.getId(), mAudio
                                .getTitle(), mAudio.getType());
                        mPresenter.downloadAudioPost(mAudio);
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
                if (share(mAudio)) {
                    //// TODO: 5/25/16 track event firebase
                }
                break;

        }
        return true;
    }

    @Override
    public void updateFavouriteState() {
        UseCaseData data = new UseCaseData();
        boolean status = !(mAudio.isFavourite() != null && mAudio.isFavourite());
        data.putBoolean(UseCaseData.FAVOURITE_STATE, status);

        AnalyticsUtil.logFavouriteEvent(getAnalytics(), mAudio.getId(), mAudio.getTitle(), mAudio
                .getType(), status);

        mFavouritePresenter.initialize(data);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle data = getIntent().getExtras();
        if (data != null) {
            if (savedInstanceState != null) {
                mAudio = (Post) savedInstanceState.get(KEY_AUDIO);
                mCurrentProgress = savedInstanceState.getInt(KEY_PROGRESS, 0);
            } else {
                mAudio = (Post) data.getSerializable(MyConstants.Extras.KEY_AUDIO);
                AnalyticsUtil.logViewEvent(getAnalytics(), mAudio.getId(), mAudio.getTitle(),
                        mAudio.getType());
            }
        }
        ((ActivityAudioDetailBinding) mBinding).setListener(this);
        updateView(mAudio);

        initialize();
        getToolbar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            mPostViewCountPresenter.initialize(null);
            mSimilarPresenter.initialize(new UseCaseData());
        }

        mediaReceiver = new MediaReceiver(this);
        receiverFilter = new IntentFilter();

        mPlayBtn.setOnClickListener(this);
        mSeekbar.setOnSeekBarChangeListener(this);

        mAppBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (mCollapsingToolbar.getHeight() + verticalOffset < 2 * ViewCompat
                        .getMinimumHeight(mCollapsingToolbar)) {
//                    mMiniPlayer.animate().alpha(1).setDuration(300);
                } else {
//                    mMiniPlayer.animate().alpha(0).setDuration(300);
                }
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            finishWithResult();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (mAudio != null) {
            menu.findItem(R.id.action_favourite).setIcon((mAudio.isFavourite() != null && mAudio
                    .isFavourite())
                    ? R.drawable.icon_favourite
                    : R.drawable.icon_not_favourite);
        }
        return true;
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
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Logger.d("AudioDetailActivity_onSaveInstanceState", "test: " + mCurrentProgress);
        outState.putSerializable(KEY_AUDIO, mAudio);
        outState.putInt(KEY_PROGRESS, mCurrentProgress);
        super.onSaveInstanceState(outState);
    }

    private void finishWithResult() {
        Intent data = new Intent();
        data.putExtra(MyConstants.Extras.KEY_FAVOURITE_STATUS, mAudio.isFavourite());
        data.putExtra(MyConstants.Extras.KEY_FAVOURITE_COUNT, mAudio.getLikes());
        data.putExtra(MyConstants.Extras.KEY_VIEW_COUNT, mAudio.getUnSyncedViewCount());
        data.putExtra(MyConstants.Extras.KEY_SHARE_COUNT, mAudio.getUnSyncedShareCount());
        setResult(RESULT_OK, data);
        finish();
    }

    private void initialize() {
        DaggerDataComponent.builder()
                .activityModule(getActivityModule())
                .applicationComponent(getApplicationComponent())
                .dataModule(new DataModule(mAudio.getId(), "audio", mAudio.getTags()))
                .build()
                .inject(this);
        mPresenter.attachView(this);
        mFavouritePresenter.attachView(this);
        mPresenter.initialize(null);
        mPostViewCountPresenter.attachView(this);
        mPostShareCountPresenter.attachView(this);
        mSimilarPresenter.attachView(this);
    }

    private void shareViaBluetooth() {
        AnalyticsUtil.logBluetoothShareEvent(getAnalytics(), mAudio.getId(), mAudio.getTitle(),
                mAudio.getType());
        mPresenter.shareViaBluetooth(mAudio);
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
    public void renderPostList(List<Post> pPosts, int pTotalCount) {
        ((ActivityAudioDetailBinding) mBinding).setSimilarAudios(pPosts);
    }

    @Override
    public void onListItemSelected(BaseModel pModel, int pIndex) {
        mAudio = (Post) pModel;
        updateView(mAudio);
        initialize();
        mPostViewCountPresenter.initialize(null);
        mSimilarPresenter.initialize(new UseCaseData());

        mService.setTrack(mAudio);
        mService.startStreaming(true);
    }

    @Override
    public void showLoadingView() {
    }

    @Override
    public void hideLoadingView() {
    }

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
        Logger.d("AudioDetailActivity_onMediaPrepared", "test: " + mCurrentProgress);
        mService.seekTo(mCurrentProgress);
        mPlayBtn.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause));
    }

    @Override
    public void onMediaComplete() {
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

    @Override
    public void onMediaProgressReset() {

    }

    @Override
    public void onMediaProgressChanged(long[] lengths) {

    }

    @Override
    public void onDismissPlayer() {

    }

    @Override
    public void onAudioDownloadStarted(String pMessage) {
        Snackbar.make(mScrollView, pMessage, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onAudioFileNotFoundToShare() {
        Snackbar.make(mScrollView, getString(R.string.error_bluetooth_share), Snackbar
                .LENGTH_SHORT).show();
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void showErrorView(String pErrorMessage) {
        Snackbar.make(mScrollView, pErrorMessage, Snackbar.LENGTH_SHORT).show();
    }

    private void updateView(Post pAudio) {
        mScrollView.scrollTo(0, 0);
        mAppBar.setExpanded(true);
        ((ActivityAudioDetailBinding) mBinding).setAudio(pAudio);
        mOldFavouriteState = mAudio.isFavourite() != null ? mAudio.isFavourite() : false;
        invalidateOptionsMenu();
    }

    private void updateSeekBar() {
        seekbarHandler.removeCallbacks(updateSeekTime);
        seekbarHandler.postDelayed(updateSeekTime, 100);
    }

    @Override
    public void renderPost(Post post) {

    }

    @Override
    public void onPostFavouriteStateUpdated(Boolean status) {
        boolean newFavouriteState = status ? !mOldFavouriteState : mOldFavouriteState;
        mAudio.setIsFavourite(newFavouriteState);
        int likes = mAudio.getLikes() == null ? 0 : mAudio.getLikes();
        mAudio.setLikes(newFavouriteState == mOldFavouriteState
                ? likes
                : newFavouriteState ? likes + 1 : likes - 1);
        ((ActivityAudioDetailBinding) mBinding).setAudio(mAudio);
        mOldFavouriteState = mAudio.isFavourite();
        invalidateOptionsMenu();
    }

    @Override
    public void onViewCountUpdated() {
        mAudio.setUnSyncedViewCount(mAudio.getUnSyncedViewCount() + 1);
    }

    @Override
    public void onShareCountUpdate(boolean status) {
        mAudio.setUnSyncedShareCount(mAudio.getUnSyncedShareCount() + 1);
        ((ActivityAudioDetailBinding) mBinding).setAudio(mAudio);
    }
}
