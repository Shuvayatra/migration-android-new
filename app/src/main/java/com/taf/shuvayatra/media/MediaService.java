package com.taf.shuvayatra.media;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.os.PowerManager;

import com.taf.data.utils.Logger;
import com.taf.model.Post;
import com.taf.shuvayatra.R;
import com.taf.util.MyConstants;

import java.io.File;
import java.io.IOException;

public class MediaService extends Service implements
        MediaPlayer.OnPreparedListener,
        MediaPlayer.OnCompletionListener,
        MediaPlayer.OnErrorListener,
        MediaPlayer.OnInfoListener {

    private final IBinder mBinder = new MusicBinder();
    private MediaPlayer mPlayer;
    private Post mTrack;
    private boolean mIsMediaValid = false;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mPlayer != null) {
            mPlayer.reset();
        } else {
            mPlayer = new MediaPlayer();
            initMediaPlayer();
        }
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
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
        mTrack = pTrack;
    }

    @Override
    public void onPrepared(MediaPlayer pMediaPlayer) {
        mIsMediaValid = true;
        pMediaPlayer.start();
        sendBroadcast(new Intent(MyConstants.Media.ACTION_STATUS_PREPARED));
    }

    @Override
    public void onCompletion(MediaPlayer pMediaPlayer) {
        sendBroadcast(new Intent(MyConstants.Media.ACTION_MEDIA_BUFFER_STOP));
        sendBroadcast(new Intent(MyConstants.Media.ACTION_MEDIA_COMPLETE));
        mIsMediaValid = false;
    }

    @Override
    public boolean onError(MediaPlayer pMediaPlayer, int what, int extra) {
        Logger.e("MediaService_onError", "info[what: " + what + ", extra: " + extra + "]");
        return false;
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        Logger.d("MediaService_onInfo", "info[what: " + what + ", extra: " + extra + "]");
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
        sendBroadcast(new Intent(MyConstants.Media.ACTION_MEDIA_BUFFER_START));
        try {
            mPlayer.reset();
            setDataSource(mTrack);
            mPlayer.prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setDataSource(Post pTrack) throws IOException {
        String mediaUrl = pTrack.getData().getMediaUrl();
        String fileName = mediaUrl.substring(mediaUrl.lastIndexOf("/") + 1);
        File root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
        File dir = new File(root, getString(R.string.app_name));
        dir.mkdirs();
        File audioFile = new File(dir, fileName);

        if (audioFile.exists()) {
            String path = audioFile.getAbsolutePath();
            if (!(path.startsWith("file") || path.startsWith("content") || path.startsWith("FILE") || path
                    .startsWith("CONTENT"))) {
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
            mIsMediaValid = true;
            mPlayer.start();
            intent.putExtra(MyConstants.Extras.KEY_PLAY_STATUS, true);
        }
        sendBroadcast(intent);
    }

    public void stopPlayback() {
        mPlayer.pause();
    }

    public void seekTo(int seekbarProgress) {
        int currentPosition = MediaHelper.progressToTimer(seekbarProgress, mPlayer.getDuration());
        mPlayer.seekTo(currentPosition);
    }

    public void startStreaming() {
        playMedia();
    }

    public boolean isMediaValid() {
        return mIsMediaValid;
    }

    public long[] getSongLengths() {
        try {
            if (mIsMediaValid) {
                return new long[]{mPlayer.getCurrentPosition(), mPlayer.getDuration()};
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

    public boolean getPlayStatus() {
        return mPlayer.isPlaying();
    }

    public class MusicBinder extends Binder {
        public MediaService getService() {
            return MediaService.this;
        }
    }
}
