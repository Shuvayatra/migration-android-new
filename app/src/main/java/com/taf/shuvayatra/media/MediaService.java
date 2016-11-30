package com.taf.shuvayatra.media;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.taf.data.utils.Logger;
import com.taf.model.Podcast;
import com.taf.model.Post;
import com.taf.shuvayatra.R;
import com.taf.shuvayatra.ui.activity.HomeActivity;
import com.taf.util.MyConstants;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class MediaService extends Service implements
        MediaPlayer.OnPreparedListener,
        MediaPlayer.OnCompletionListener,
        MediaPlayer.OnErrorListener,
        MediaPlayer.OnInfoListener {

    public static final int NOTIFICATION_ID = 1234;

    private final IBinder mBinder = new MusicBinder();
    Handler mHandler = new Handler();
    NotificationManager manager;
    NotificationEventReceiver notificationEventReceiver;
    private MediaPlayer mPlayer;
    private Post mTrack;
    private List<Podcast> mPodcasts;
    private boolean mIsMediaValid = false;
    private PlayType mCurrentPlayType;
    private int mCurrentPodcastIndex = 0;
    private long mCurrentDuration = 0;
    private boolean mStoppedByUser = false;
    private String mCurrentTitle = "";
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
//            mPlayer.reset();
        } else {
            mPlayer = new MediaPlayer();
            initMediaPlayer();
        }
        mHandler.postDelayed(updateSeekTime, 1000);

        IntentFilter filter = new IntentFilter();
        filter.addAction(NotificationEventReceiver.ACTION_DISMISS);
        filter.addAction(NotificationEventReceiver.ACTION_PAUSE_PLAY);
        notificationEventReceiver = new NotificationEventReceiver();

        registerReceiver(notificationEventReceiver, filter);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Logger.d("MediaService_onDestroy", "test");
        super.onDestroy();
        unregisterReceiver(notificationEventReceiver);
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

            createNotification(mCurrentTitle, true);

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
        createNotification(mCurrentTitle, mPlayer.isPlaying());
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

    private void createNotification(String title, boolean isPlaying) {
        manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent notificationIntent = new Intent(MediaService.this, HomeActivity.class);
        PendingIntent toOpen = PendingIntent.getActivity(getApplicationContext(), 0,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent cancelNotification = new Intent(NotificationEventReceiver.ACTION_DISMISS);
        PendingIntent cancel = PendingIntent
                .getBroadcast(getApplicationContext(), 0, cancelNotification, 0);

        Intent pausePlayNotification = new Intent(NotificationEventReceiver.ACTION_PAUSE_PLAY);
        PendingIntent pausePlay = PendingIntent
                .getBroadcast(getApplicationContext(), 0, pausePlayNotification, 0);

        RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.view_notification);
        contentView.setTextViewText(R.id.notificationTitle, title);

        if (isPlaying) {
            contentView.setImageViewResource(R.id.playpause, R.drawable.ic_pause);
        } else {
            contentView.setImageViewResource(R.id.playpause, R.drawable.ic_play);
        }
        contentView.setOnClickPendingIntent(R.id.playpause, pausePlay);
        contentView.setOnClickPendingIntent(R.id.cancel, cancel);

        Notification notification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_logo)
                .setContentIntent(toOpen)
                .setContent(contentView)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setLights(0x000000FF, 300, 1000).build();

        startForeground(NOTIFICATION_ID, notification);
    }

    public enum PlayType {
        POST,
        PODCAST
    }

    private class NotificationEventReceiver extends BroadcastReceiver {
        private static final String ACTION_DISMISS = "action.NOTIFICATION_DISMISS";
        private static final String ACTION_PAUSE_PLAY = "action.NOTIFICATION_PLAY_PAUSE";

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ACTION_DISMISS)) {
                stopPlayback();
                stopForeground(true);
//                stopSelf();
            } else if (intent.getAction().equals(ACTION_PAUSE_PLAY)) {
                playPause();
            }
        }
    }

    public class MusicBinder extends Binder {
        public MediaService getService() {
            return MediaService.this;
        }
    }
}
