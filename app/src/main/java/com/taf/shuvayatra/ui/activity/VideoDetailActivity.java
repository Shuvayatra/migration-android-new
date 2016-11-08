package com.taf.shuvayatra.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeIntents;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.taf.data.utils.Logger;
import com.taf.model.Post;
import com.taf.shuvayatra.MyApplication;
import com.taf.shuvayatra.R;
import com.taf.shuvayatra.base.PostDetailActivity;
import com.taf.shuvayatra.databinding.VideoDetailDataBinding;
import com.taf.util.MyConstants;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;

public class VideoDetailActivity extends PostDetailActivity implements
        SwipeRefreshLayout.OnRefreshListener,
        YouTubePlayer.OnInitializedListener {

    Boolean IsFromIntent = false;
    @BindView(R.id.swipe_container)
    SwipeRefreshLayout mContainer;
    private YouTubePlayerSupportFragment mYouTubePlayerFragment;

    @Override
    public int getLayout() {
        return R.layout.activity_video_detail;
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
    protected void updateView(Post post) {
        ((VideoDetailDataBinding) mBinding).setVideo(post);
        ((VideoDetailDataBinding) mBinding).setSimilarVideos(post.getSimilarPosts());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((MyApplication) getApplicationContext()).mService.stopPlayback();

        mYouTubePlayerFragment = (YouTubePlayerSupportFragment) getSupportFragmentManager()
                .findFragmentById(R.id.youtube_fragment);
        mYouTubePlayerFragment.initialize(MyConstants.YOUTUBE_API_KEY, this);
    }

    @Override
    public void onRefresh() {
        loadPost();
    }

    @Override
    public void showLoadingView() {
        mContainer.setRefreshing(true);
    }

    @Override
    public void hideLoadingView() {
        mContainer.setRefreshing(false);
    }
}
