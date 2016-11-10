package com.taf.shuvayatra.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeIntents;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.taf.data.utils.Logger;
import com.taf.model.BaseModel;
import com.taf.model.Post;
import com.taf.shuvayatra.MyApplication;
import com.taf.shuvayatra.R;
import com.taf.shuvayatra.base.PostDetailActivity;
import com.taf.shuvayatra.databinding.VideoDetailDataBinding;
import com.taf.shuvayatra.ui.interfaces.ListItemClickListener;
import com.taf.util.MyConstants;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;

public class VideoDetailActivity extends PostDetailActivity implements
        SwipeRefreshLayout.OnRefreshListener,
        YouTubePlayer.OnInitializedListener,
        ListItemClickListener {

    Boolean IsFromIntent = false;
    @BindView(R.id.swipe_container)
    SwipeRefreshLayout mContainer;
    @BindView(R.id.similar_story_list)
    RecyclerView recyclerView;
    private YouTubePlayerSupportFragment mYouTubePlayerFragment;
    private YouTubePlayer player;

    @Override
    public int getLayout() {
        return R.layout.activity_video_detail;
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider pProvider, YouTubePlayer pYouTubePlayer, boolean isRestored) {

        pYouTubePlayer.setFullscreenControlFlags(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_ORIENTATION);
        pYouTubePlayer.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_ALWAYS_FULLSCREEN_IN_LANDSCAPE);
        pYouTubePlayer.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_SYSTEM_UI);

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

        player = pYouTubePlayer;
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
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setTitle(post.getTitle());
        }
        ((VideoDetailDataBinding) mBinding).setVideo(post);
        ((VideoDetailDataBinding) mBinding).setListener(this);
        ((VideoDetailDataBinding) mBinding).setSimilarStories(post.getSimilarPosts());
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((MyApplication) getApplicationContext()).mService.stopPlayback();


        mYouTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();
        mYouTubePlayerFragment.setRetainInstance(true);
        mYouTubePlayerFragment.initialize(MyConstants.YOUTUBE_API_KEY, this);
        // add fragment to transaction
        getSupportFragmentManager().beginTransaction().add(R.id.youtube_container,
                mYouTubePlayerFragment).commit();

        mContainer.setOnRefreshListener(this);
    }

    private static final String TAG = "VideoDetailActivity";

    @Override
    public void onRefresh() {
        loadPost();
        // restart player
        if (player != null) {
            player.release();
            mYouTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();
            mYouTubePlayerFragment.initialize(MyConstants.YOUTUBE_API_KEY, this);
            getSupportFragmentManager().beginTransaction().replace(R.id.youtube_container,
                    mYouTubePlayerFragment).commit();
        }
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
