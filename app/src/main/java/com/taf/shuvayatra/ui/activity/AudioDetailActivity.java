package com.taf.shuvayatra.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.taf.data.utils.Logger;
import com.taf.model.BaseModel;
import com.taf.model.Post;
import com.taf.shuvayatra.MyApplication;
import com.taf.shuvayatra.R;
import com.taf.shuvayatra.base.PostDetailActivity;
import com.taf.shuvayatra.databinding.ActivityAudioDetailBinding;
import com.taf.shuvayatra.media.MediaHelper;
import com.taf.shuvayatra.media.MediaReceiver;
import com.taf.shuvayatra.ui.fragment.MiniPlayerFragment;
import com.taf.shuvayatra.ui.interfaces.ListItemClickListener;
import com.taf.shuvayatra.ui.views.AudioDetailView;
import com.taf.shuvayatra.ui.views.AudioPlayerView;
import com.taf.shuvayatra.util.AnalyticsUtil;
import com.taf.util.MyConstants;

import java.io.IOException;

import butterknife.BindView;

public class AudioDetailActivity extends PostDetailActivity implements
        AudioDetailView,
        AudioPlayerView,
        View.OnClickListener,
        ListItemClickListener,
        SeekBar.OnSeekBarChangeListener,
        AppBarLayout.OnOffsetChangedListener {

    private static final int MY_PERMISSIONS_REQUEST_WRITE_STORAGE = 123;
    private static final int GROUP1 = 101;
    private static final int SUBMENU_BLUETOOTH = 1001;
    private static final int SUBMENU_FACEBOOK = 1002;
    private static final String KEY_PROGRESS = "key_progress";
    private static final String TAG = "AudioDetailActivity";
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
    @BindView(R.id.overlay)
    RelativeLayout mOverlay;
    MiniPlayerFragment mPlayerFragment;
    private MediaReceiver mediaReceiver;
    private IntentFilter receiverFilter;
    private int mCurrentProgress;
    private boolean seekbarChangeByUser = false;
    private Post mainPost;

    @Override
    public int getLayout() {
        return R.layout.activity_audio_detail;
    }

    @Override
    public void onPause() {
        if (mediaReceiver != null)
            unregisterReceiver(mediaReceiver);
        super.onPause();
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
                ((MyApplication) getApplicationContext()).mService.playPause();
                break;
            default:
                break;
        }
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

        if (appBarLayout.getTotalScrollRange() == Math.abs(verticalOffset)) {
            if (mainPost != null && getToolbar().getTitle() == null)
                getToolbar().setTitle(mainPost.getTitle());
        } else {
            if (getToolbar().getTitle() != null) getToolbar().setTitle(null);
        }


        if (mPlayerFragment != null) {
            if (mCollapsingToolbar.getHeight() + verticalOffset < 2 * ViewCompat
                    .getMinimumHeight(mCollapsingToolbar)) {
                getSupportFragmentManager().beginTransaction()
                        .show(mPlayerFragment)
                        .commit();
            } else {
                getSupportFragmentManager().beginTransaction()
                        .hide(mPlayerFragment)
                        .commit();
            }
        }
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
        ((MyApplication) getApplicationContext()).mService.seekTo(mCurrentProgress);
        seekbarChangeByUser = false;
    }

    @Override
    public void onListItemSelected(BaseModel pModel, int pIndex) {

        Intent intent = null;

        if (pModel.getDataType() == MyConstants.Adapter.TYPE_AUDIO) {
            intent = new Intent(this, AudioDetailActivity.class);
        } else if (pModel.getDataType() == MyConstants.Adapter.TYPE_VIDEO) {
            intent = new Intent(this, VideoDetailActivity.class);
        } else if (pModel.getDataType() == MyConstants.Adapter.TYPE_NEWS || pModel.getDataType()
                == MyConstants.Adapter.TYPE_TEXT) {
            intent = new Intent(this, ArticleDetailActivity.class);
        }

        if (intent != null) {
            intent.putExtra(MyConstants.Extras.KEY_ID, pModel.getId());
            startActivity(intent);
        }
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
    public void onBufferStarted() {
        bufferingText.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBufferStopped() {
        bufferingText.setVisibility(View.GONE);
    }

    @Override
    public void onMediaPrepared() {
        Logger.d("AudioDetailActivity_onMediaPrepared", "progress: " + mCurrentProgress);
        ((MyApplication) getApplicationContext()).mService.seekTo(mCurrentProgress);
        mPlayBtn.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause));
    }

    @Override
    public void onMediaComplete() {
        mPlayBtn.setImageDrawable(getResources().getDrawable(R.drawable.ic_play));
    }

    @Override
    public void playStatusChanged(boolean pIsPlaying) {
        mPlayBtn.setImageDrawable(getResources().getDrawable(pIsPlaying ? R.drawable.ic_pause : R
                .drawable.ic_play));
    }

    public void onMediaProgressReset() {
        if (!seekbarChangeByUser) {
            mSeekbar.setProgress(0);
        }
    }

    @Override
    public void onMediaProgressChanged(long[] lengths) {
        if (!seekbarChangeByUser) {
            mAudioTime.setText(getString(R.string.audio_time, MediaHelper
                    .getFormattedTime(lengths[0]), MediaHelper.getFormattedTime
                    (lengths[1])));
            mCurrentProgress = MediaHelper.getProgressPercentage(lengths[0], lengths[1]);
            mSeekbar.setProgress(mCurrentProgress);
        }
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
    public void updateView(Post post) {
        mainPost = post;
        ((MyApplication) getApplicationContext()).mService.setTrack(post);

        Logger.e(TAG, ">>> COLLAPSING TOOLBAR TITLE: " + mCollapsingToolbar.getTitle() + " <<<");

        mScrollView.scrollTo(0, 0);
        mAppBar.setExpanded(true);
        ((ActivityAudioDetailBinding) mBinding).setAudio(post);
        ((ActivityAudioDetailBinding) mBinding).setSimilarAudios(post.getSimilarPosts());
        invalidateOptionsMenu();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mCurrentProgress = savedInstanceState.getInt(KEY_PROGRESS, 0);
        } else {
            mCurrentProgress = 0;
        }

        ((ActivityAudioDetailBinding) mBinding).setListener(this);
        initialize();

        mediaReceiver = new MediaReceiver(this);
        receiverFilter = new IntentFilter();

        mPlayBtn.setOnClickListener(this);
        mSeekbar.setOnSeekBarChangeListener(this);

        mAppBar.addOnOffsetChangedListener(this);
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
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(KEY_PROGRESS, mCurrentProgress);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean alwaysShowPlayer() {
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mPost != null) {
            ((MyApplication) getApplicationContext()).mService.setTrack(mPost);
        }

        receiverFilter.addAction(MyConstants.Media.ACTION_MEDIA_BUFFER_START);
        receiverFilter.addAction(MyConstants.Media.ACTION_MEDIA_BUFFER_STOP);
        receiverFilter.addAction(MyConstants.Media.ACTION_STATUS_PREPARED);
        receiverFilter.addAction(MyConstants.Media.ACTION_PLAY_STATUS_CHANGE);
        receiverFilter.addAction(MyConstants.Media.ACTION_MEDIA_PLAYBACK_CHANGE);
        receiverFilter.addAction(MyConstants.Media.ACTION_PROGRESS_CHANGE);
        registerReceiver(mediaReceiver, receiverFilter);
    }

    private void downloadAudio() throws IOException {
        mPresenter.downloadAudioPost(mPost);
        AnalyticsUtil.logDownloadEvent(getAnalytics(), mPost.getId(), mPost
                .getTitle(), mPost.getType());
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
                        downloadAudio();
                    } catch (IOException e) {
                        Snackbar.make(mBinding.getRoot(), "Could not download the audio. Try " +
                                "again later.", Snackbar.LENGTH_LONG).show();
                    }
                }
                return;
        }
    }

    public void requestForPermissions() throws IOException {
        Logger.d("AudioDetailActivity_requestForPermissions", "request permission");
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
                    .WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_STORAGE);
        } else {
            downloadAudio();
        }
    }
}
