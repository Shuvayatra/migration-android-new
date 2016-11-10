package com.taf.shuvayatra.ui.deprecated.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeIntents;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.taf.data.utils.Logger;
import com.taf.interactor.UseCaseData;
import com.taf.model.Post;
import com.taf.shuvayatra.R;
import com.taf.shuvayatra.base.FacebookActivity;
import com.taf.shuvayatra.databinding.VideoDetailDataBinding;
import com.taf.shuvayatra.di.component.DaggerDataComponent;
import com.taf.shuvayatra.di.module.DataModule;
import com.taf.shuvayatra.presenter.deprecated.PostFavouritePresenter;
import com.taf.shuvayatra.presenter.deprecated.PostViewCountPresenter;
import com.taf.shuvayatra.presenter.deprecated.SimilarPostPresenter;
import com.taf.shuvayatra.ui.views.PostDetailView;
import com.taf.shuvayatra.ui.deprecated.interfaces.PostListView;
import com.taf.shuvayatra.util.AnalyticsUtil;
import com.taf.util.MyConstants;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

public class VideoDetailActivity extends FacebookActivity implements
        YouTubePlayer.OnInitializedListener,
        PostDetailView,
        PostListView {

    private static final String KEY_VIDEO = "key_video";
    @Inject
    SimilarPostPresenter mSimilarPresenter;
    @Inject
    PostFavouritePresenter mFavouritePresenter;
    @Inject
    PostViewCountPresenter mPostViewCountPresenter;

    Post mPost;
    boolean mOldFavouriteState;
    Boolean IsFromIntent = false;
    private YouTubePlayerSupportFragment mYouTubePlayerFragment;

    @Override
    public int getLayout() {
        return R.layout.activity_video_detail;
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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finishWithResult();
            return true;
        } else if (item.getItemId() == R.id.action_share) {
            share(mPost);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void updateFavouriteState() {
        UseCaseData data = new UseCaseData();
        boolean status = !(mPost.isFavourite() != null && mPost.isFavourite());
        data.putBoolean(UseCaseData.FAVOURITE_STATE, status);

        AnalyticsUtil.logFavouriteEvent(getAnalytics(), mPost.getId(), mPost.getTitle(), mPost
                .getType(), status);

        mFavouritePresenter.initialize(data);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (savedInstanceState != null) {
                mPost = (Post) savedInstanceState.get(KEY_VIDEO);
            } else {
                mPost = (Post) bundle.getSerializable(MyConstants.Extras.KEY_VIDEO);
                AnalyticsUtil.logViewEvent(getAnalytics(), mPost.getId(), mPost.getTitle(), mPost
                        .getType());
            }
        }
        ((VideoDetailDataBinding) mBinding).setVideo(mPost);
        mOldFavouriteState = mPost.isFavourite() != null ? mPost.isFavourite() : false;
        initialize();
        if (savedInstanceState == null) {
            mPostViewCountPresenter.initialize(null);
            mSimilarPresenter.initialize(new UseCaseData());
        }
//        mYouTubePlayerFragment = (YouTubePlayerSupportFragment) getSupportFragmentManager().findFragmentById(R.id.youtube_fragment);
        mYouTubePlayerFragment.initialize(MyConstants.YOUTUBE_API_KEY, this);
    }

    private void initialize() {
        DaggerDataComponent.builder()
                .activityModule(getActivityModule())
                .applicationComponent(getApplicationComponent())
                .dataModule(new DataModule(mPost.getId(), "video", mPost.getTags()))
                .build()
                .inject(this);
        mFavouritePresenter.attachView(this);
        mPostViewCountPresenter.attachView(this);
        mPostShareCountPresenter.attachView(this);
        mSimilarPresenter.attachView(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            finishWithResult();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void finishWithResult() {
        Intent data = new Intent();
        data.putExtra(MyConstants.Extras.KEY_FAVOURITE_STATUS, mPost.isFavourite());
        data.putExtra(MyConstants.Extras.KEY_VIEW_COUNT, mPost.getUnSyncedViewCount());
        data.putExtra(MyConstants.Extras.KEY_FAVOURITE_COUNT, mPost.getLikes());
        data.putExtra(MyConstants.Extras.KEY_SHARE_COUNT, mPost.getUnSyncedShareCount());
        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public void renderPostList(List<Post> pPosts, int pTotalCount) {
        ((VideoDetailDataBinding) mBinding).setSimilarStories(pPosts);
    }

    @Override
    public void showLoadingView() {
    }

    @Override
    public void hideLoadingView() {
    }

    @Override
    public void onPostFavouriteStateUpdated(Boolean status) {
        boolean newFavouriteState = status ? !mOldFavouriteState : mOldFavouriteState;
        mPost.setIsFavourite(newFavouriteState);
        int likes = mPost.getLikes() == null ? 0 : mPost.getLikes();
        mPost.setLikes(newFavouriteState == mOldFavouriteState
                ? likes
                : newFavouriteState ? likes + 1 : likes - 1);
        ((VideoDetailDataBinding) mBinding).setVideo(mPost);
        mOldFavouriteState = mPost.isFavourite();
        invalidateOptionsMenu();
    }

    @Override
    public void onViewCountUpdated() {
        mPost.setUnSyncedViewCount(mPost.getUnSyncedViewCount() + 1);
    }

    @Override
    public void onShareCountUpdate(boolean status) {
        mPost.setUnSyncedShareCount(mPost.getUnSyncedShareCount() + 1);
        ((VideoDetailDataBinding) mBinding).setVideo(mPost);
    }

    @Override
    public void showErrorView(String pErrorMessage) {
        Snackbar.make(mBinding.getRoot(), pErrorMessage, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider pProvider, YouTubePlayer pYouTubePlayer, boolean isRestored) {

        pYouTubePlayer.setFullscreenControlFlags(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_ORIENTATION);
        pYouTubePlayer.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_ALWAYS_FULLSCREEN_IN_LANDSCAPE);
        pYouTubePlayer.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_SYSTEM_UI);
//        pYouTubePlayer.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT);

        final String videoId = getYoutubeIdFromUrl(mPost.getData().getMediaUrl());
        if (!isRestored) {
            if (!videoId.isEmpty())
                Logger.e("VideoDetailActivity", "video loaded");
            pYouTubePlayer.loadVideo(videoId);
            pYouTubePlayer.setPlayerStateChangeListener(new YouTubePlayer.PlayerStateChangeListener() {
                @Override
                public void onLoading() {
                    Logger.e("VideoDetailActivity", " video loading");
                }

                @Override
                public void onLoaded(String pS) {
                    Logger.e("VideoDetailActivity", " video loaded " + pS);

                }

                @Override
                public void onAdStarted() {

                }

                @Override
                public void onVideoStarted() {
                    Logger.e("VideoDetailActivity", " video started");

                }

                @Override
                public void onVideoEnded() {
                    Logger.e("VideoDetailActivity", " video ended");

                }

                @Override
                public void onError(YouTubePlayer.ErrorReason pErrorReason) {
                    Logger.e("VideoDetailActivity", " video error: " + pErrorReason);
                    if (!IsFromIntent) {
                        IsFromIntent = true;
                        playViaIntent(videoId);
                    }
//                    Snackbar.make(mBinding.getRoot(), R.string.youtube_error, Snackbar.LENGTH_LONG)
//                            .setAction("Open", new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    playViaIntent(mPost.getData().getMediaUrl());
//                                }
//                            }).show();

                }
            });
        } else {
            Logger.e("VideoDetailActivity", "video played");
            pYouTubePlayer.play();
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider pProvider, YouTubeInitializationResult pYouTubeInitializationResult) {
        Logger.e("VideoDetailActivity", "error initializing error");
    }

    private void playViaIntent(String url) {
        if (YouTubeIntents.canResolvePlayVideoIntent(this)) {
            Logger.e("VideoDetailActivity", "play via intent");
            Intent intent = YouTubeIntents.createPlayVideoIntent(this, url);
            startActivity(intent);
        } else {
            Logger.e("VideoDetailActivity", "cant play via intent");
        }
    }

    public String getYoutubeIdFromUrl(String url) {
        String pattern = "(?<=watch\\?v=|/videos/|embed\\/)[^#\\&\\?]*";

        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(url);

        if (matcher.find()) {
            return matcher.group();
        }
        return "";
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (containsFavouriteOption()) {
            menu.findItem(R.id.action_favourite).setIcon((mPost.isFavourite() != null && mPost
                    .isFavourite()) ? R.drawable
                    .icon_favourite : R.drawable.icon_not_favourite);
            return true;
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(KEY_VIDEO, mPost);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void renderPost(Post post) {

    }
}
