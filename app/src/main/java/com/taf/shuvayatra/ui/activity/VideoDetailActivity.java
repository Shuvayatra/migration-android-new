package com.taf.shuvayatra.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.MenuItem;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.taf.data.utils.Logger;
import com.taf.model.Post;
import com.taf.shuvayatra.R;
import com.taf.shuvayatra.base.BaseActivity;
import com.taf.shuvayatra.base.FacebookActivity;
import com.taf.shuvayatra.databinding.VideoDetailDataBinding;
import com.taf.shuvayatra.di.component.DaggerDataComponent;
import com.taf.shuvayatra.di.module.DataModule;
import com.taf.shuvayatra.presenter.PostFavouritePresenter;
import com.taf.shuvayatra.ui.interfaces.PostDetailView;
import com.taf.util.MyConstants;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

public class VideoDetailActivity extends FacebookActivity implements
        YouTubePlayer.OnInitializedListener,
        PostDetailView{

    @Inject
    PostFavouritePresenter mFavouritePresenter;

    Post mPost;
    boolean mOldFavouriteState;
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
    public boolean containsFavouriteOption() {
        return true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mPost = (Post) bundle.getSerializable(MyConstants.Extras.KEY_VIDEO);
        }
        ((VideoDetailDataBinding) mBinding).setVideo(mPost);
        mOldFavouriteState = mPost.isFavourite() != null ? mPost.isFavourite() : false;
        initialize();

        mYouTubePlayerFragment = (YouTubePlayerSupportFragment) getSupportFragmentManager().findFragmentById(R.id.youtube_fragment);
        mYouTubePlayerFragment.initialize(MyConstants.YOUTUBE_API_KEY, this);
    }

    private void initialize() {
        DaggerDataComponent.builder()
                .activityModule(getActivityModule())
                .applicationComponent(getApplicationComponent())
                .dataModule(new DataModule(mPost.getId()))
                .build()
                .inject(this);
        mFavouritePresenter.attachView(this);
    }

    @Override
    public void onPostFavouriteStateUpdated(Boolean status) {
        mPost.setIsFavourite(status ? !mOldFavouriteState : mOldFavouriteState);
        invalidateOptionsMenu();
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


        String videoId = getYoutubeIdFromUrl("https://www.youtube.com/watch?v=BiP4BxYjH08");
        if (!isRestored) {
            if (!videoId.isEmpty())
                pYouTubePlayer.loadVideo(videoId);
        } else {
            pYouTubePlayer.play();
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider pProvider, YouTubeInitializationResult pYouTubeInitializationResult) {
        Logger.e("VideoDetailActivity", "error initializing error");
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
    public boolean containsShareOption() {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }else if (item.getItemId() == R.id.action_share) {
            showShareDialog(mPost);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
