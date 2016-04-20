package com.taf.shuvayatra.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.taf.data.utils.Logger;
import com.taf.model.Post;
import com.taf.shuvayatra.R;
import com.taf.shuvayatra.base.BaseActivity;
import com.taf.shuvayatra.databinding.VideoDetailDataBinding;
import com.taf.util.MyConstants;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VideoDetailActivity extends BaseActivity implements YouTubePlayer.OnInitializedListener {

    Post mPost;
    private YouTubePlayerSupportFragment mYouTubePlayerFragment;

    @Override
    public int getLayout() {
        return R.layout.activity_video_detail;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            mPost = (Post) bundle.getSerializable(MyConstants.Extras.KEY_VIDEO);

        }
        ((VideoDetailDataBinding) mBinding).setVideo(mPost);
        mYouTubePlayerFragment = (YouTubePlayerSupportFragment) getSupportFragmentManager().findFragmentById(R.id.youtube_fragment);
        mYouTubePlayerFragment.initialize(MyConstants.YOUTUBE_API_KEY, this);
    }

    @Override
    public boolean isDataBindingEnabled() {
        return true;
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
        }
        else{
            pYouTubePlayer.play();
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
    public void onInitializationFailure(YouTubePlayer.Provider pProvider, YouTubeInitializationResult pYouTubeInitializationResult) {
        Logger.e("VideoDetailActivity", "error initializing error");
    }
}
