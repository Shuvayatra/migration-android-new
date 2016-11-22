package com.taf.shuvayatra.media;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;

import com.taf.data.utils.AppPreferences;
import com.taf.data.utils.Logger;
import com.taf.model.Podcast;
import com.taf.model.Post;
import com.taf.shuvayatra.R;
import com.taf.util.MyConstants;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class MediaService extends Service implements
        MediaPlayer.OnPreparedListener,
        MediaPlayer.OnCompletionListener,
        MediaPlayer.OnErrorListener,
        MediaPlayer.OnInfoListener {

    private final IBinder mBinder = new MusicBinder();
    Handler mHandler = new Handler();
    private MediaPlayer mPlayer;
    private Post mTrack;
    private List<Podcast> mPodcasts;
    private boolean mIsMediaValid = false;
    private PlayType mCurrentPlayType;
    private int mCurrentPodcastIndex = 0;
    private long mCurrentDuration = 0;
    private boolean mStoppedByUser = false;

    private String mCurrentTitle = "";

    private AppPreferences mPreferences;

    private Runnable updateSeekTime = new Runnable() {
        @Override
        public void run() {
            if (isMediaValid()) {
                long[] lengths = getSongLengths();
                Intent intent = new Intent(MyConstants.Media.ACTION_PROGRESS_CHANGE);
                if (lengths != null) {
                    intent.putExtra("lengths", lengths);
                }
                sendBroadcast(intent);
            }
            mHandler.postDelayed(this, 500);
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Logger.d("MediaService_onStartCommand", "test");
        if (mPlayer != null) {
            mPlayer.reset();
        } else {
            mPlayer = new MediaPlayer();
            initMediaPlayer();
        }
        mHandler.postDelayed(updateSeekTime, 1000);
        mPreferences = new AppPreferences(getApplicationContext());
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        Logger.d("MediaService_onDestroy", "test");
        super.onDestroy();
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Logger.d("MediaService_onUnbind", "test");
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
        }
        return true;
    }

    private void initMediaPlayer() {
        mPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mPlayer.setOnPreparedListener(this);
        mPlayer.setOnCompletionListener(this);
        mPlayer.setOnErrorListener(this);
        mPlayer.setOnInfoListener(this);
    }

    public void setTrack(Post pTrack) {
        stopPlayback();
        mTrack = pTrack;
        mCurrentPlayType = PlayType.POST;
        startStreaming();
    }

    public void setPodcasts(List<Podcast> podcasts) {
        stopPlayback();
        mPodcasts = podcasts;
        mCurrentPlayType = PlayType.PODCAST;
        mCurrentPodcastIndex = 0;
        startStreaming();
    }

    @Override
    public void onPrepared(MediaPlayer pMediaPlayer) {
        mHandler.postDelayed(updateSeekTime, 500);
        pMediaPlayer.start();
        pMediaPlayer.seekTo(0);
        Logger.d("MediaService_onPrepared", "test prepared");
        sendBroadcast(new Intent(MyConstants.Media.ACTION_STATUS_PREPARED));
        mCurrentDuration = pMediaPlayer.getDuration();
        mIsMediaValid = true;
    }

    @Override
    public void onCompletion(MediaPlayer pMediaPlayer) {
        sendBroadcast(new Intent(MyConstants.Media.ACTION_MEDIA_BUFFER_STOP));
        sendBroadcast(new Intent(MyConstants.Media.ACTION_MEDIA_COMPLETE));

        mHandler.removeCallbacks(updateSeekTime);
        mIsMediaValid = false;

        if (mCurrentPlayType.equals(PlayType.PODCAST) && !mStoppedByUser) {
            mCurrentPodcastIndex++;
            if (mCurrentPodcastIndex == mPodcasts.size()) mCurrentPodcastIndex = 0;
            playMedia();
        }
    }

    @Override
    public boolean onError(MediaPlayer pMediaPlayer, int what, int extra) {
        Logger.e("MediaService_onError", "info[what: " + what + ", extra: " + extra + "]");
        return false;
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        switch (what) {
            case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                sendBroadcast(new Intent(MyConstants.Media.ACTION_MEDIA_BUFFER_STOP));
                break;
            case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                sendBroadcast(new Intent(MyConstants.Media.ACTION_MEDIA_BUFFER_START));
                break;
        }
        return true;
    }

    private void playMedia() {
        try {
            mPlayer.reset();
            Logger.d("MediaService_playMedia", "test: " + mCurrentPodcastIndex);
            if (mCurrentPlayType.equals(PlayType.POST)) setDataSource(mTrack);
            else setDataSource(mPodcasts.get(mCurrentPodcastIndex));

            mPlayer.prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setDataSource(Podcast podcast) throws IOException {
        mCurrentTitle = podcast.getTitle();
        String mediaUrl = podcast.getSource().replace(" ", "%20");
        mPlayer.setDataSource(this, Uri.parse(mediaUrl));
    }

    private void setDataSource(Post pTrack) throws IOException {
        mCurrentTitle = pTrack.getTitle();
        String mediaUrl = pTrack.getData().getMediaUrl().replace(" ", "%20");
        String fileName = mediaUrl.substring(mediaUrl.lastIndexOf("/") + 1).replace("%20", " ");
        File root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
        File dir = new File(root, getString(R.string.app_name));
        dir.mkdir();
        File audioFile = new File(dir, fileName);

        if (audioFile.exists()) {
            String path = audioFile.getAbsolutePath();
            if (!(path.startsWith("file") || path.startsWith("content") || path.startsWith
                    ("FILE") || path.startsWith("CONTENT"))) {
                path = "file://" + path;
            }
            mPlayer.setDataSource(this, Uri.parse(path));
        } else {
            mPlayer.setDataSource(mediaUrl.replace(" ", "%20"));
        }
    }

    public void playPause() {
        Intent intent = new Intent(MyConstants.Media.ACTION_PLAY_STATUS_CHANGE);
        if (mPlayer.isPlaying()) {
            mPlayer.pause();
            intent.putExtra(MyConstants.Extras.KEY_PLAY_STATUS, false);
        } else {
            if (!mIsMediaValid) {
                playMedia();
            } else {
                mIsMediaValid = true;
                mPlayer.start();
                intent.putExtra(MyConstants.Extras.KEY_PLAY_STATUS, true);
            }
        }
        sendBroadcast(intent);
    }

    public void stopPlayback() {
        mCurrentTitle = " ---- ";
        mStoppedByUser = true;
        if (mIsMediaValid)
            mPlayer.stop();

        Intent intent = new Intent(MyConstants.Media.ACTION_PLAY_STATUS_CHANGE);
        intent.putExtra(MyConstants.Extras.KEY_PLAY_STATUS, false);
        sendBroadcast(intent);

        mIsMediaValid = false;
    }

    public void seekTo(int seekbarProgress) {
        int currentPosition = MediaHelper.progressToTimer(seekbarProgress, mPlayer.getDuration());
        mPlayer.seekTo(currentPosition);
    }

    public void startStreaming() {
        mStoppedByUser = false;
        mCurrentPodcastIndex = 0;
        playMedia();
    }

    public boolean isMediaValid() {
        return mIsMediaValid;
    }

    public long[] getSongLengths() {
        try {
            if (mIsMediaValid) {
                return new long[]{mPlayer.getCurrentPosition(), mCurrentDuration};
            } else {
                return new long[]{0, 0};
            }
        } catch (IllegalStateException e) {
            return new long[]{0, 0};
        }
    }

    public Post getCurrentTrack() {
        return mTrack;
    }

    public Podcast getCurrentPodcast() {
        return mPodcasts.get(mCurrentPodcastIndex);
    }

    public boolean getPlayStatus() {
        return mPlayer.isPlaying();
    }

    public String getCurrentTitle() {
        return mCurrentTitle;
    }

    public void changeCurrentPodcast(int index) {
        if (mCurrentPlayType.equals(PlayType.PODCAST)) {
            stopPlayback();
            mCurrentPodcastIndex = index;
            playMedia();
        }
    }

    public enum PlayType {
        POST,
        PODCAST
    }

    public class MusicBinder extends Binder {
        public MediaService getService() {
            return MediaService.this;
        }
    }
}
