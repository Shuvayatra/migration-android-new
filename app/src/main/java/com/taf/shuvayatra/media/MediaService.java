package com.taf.shuvayatra.media;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.session.MediaController;
import android.media.session.MediaSession;
import android.media.session.MediaSessionManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v7.widget.AppCompatDrawableManager;
import android.util.Log;
import android.widget.RemoteViews;

import com.taf.data.utils.Logger;
import com.taf.model.Podcast;
import com.taf.model.Post;
import com.taf.shuvayatra.R;
import com.taf.shuvayatra.receivers.NotificationBroadcastReceiver;
import com.taf.shuvayatra.ui.activity.HomeActivity;
import com.taf.util.MyConstants;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The type Media service.
 */
public class MediaService extends Service implements
        MediaPlayer.OnPreparedListener,
        MediaPlayer.OnCompletionListener,
        MediaPlayer.OnErrorListener,
        MediaPlayer.OnInfoListener {

    public static final String TAG = "MediaService";

    public static final int NOTIFICATION_ID = 1234;

    private final IBinder mBinder = new MusicBinder();
    Handler mHandler = new Handler();
    NotificationManager manager;
    NotificationEventReceiver notificationEventReceiver;
    private MediaPlayer mPlayer;
    private Post mTrack;
    private List<Podcast> mPodcasts;
    private boolean mIsMediaValid = false;
    private PlayType mCurrentPlayType = PlayType.NONE;
    private int mCurrentPodcastIndex = 0;
    private long mCurrentDuration = 0;
    private boolean mStoppedByUser = false;
    private String mCurrentTitle = "";
    private String mCurrentImage = "";
    private String mCurrentDescription = "";
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
        Log.d(TAG, "onStartCommand() called with: intent = [" + intent + "], flags = [" + flags + "], startId = [" + startId + "]");
        if (mPlayer == null) {
            mPlayer = new MediaPlayer();
            initMediaPlayer();
            IntentFilter filter = new IntentFilter();
            filter.addAction(NotificationEventReceiver.ACTION_DISMISS);
            filter.addAction(NotificationEventReceiver.ACTION_PAUSE_PLAY);
            notificationEventReceiver = new NotificationEventReceiver();
            registerReceiver(notificationEventReceiver, filter);
        }
        mHandler.postDelayed(updateSeekTime, 1000);
        return START_NOT_STICKY;
    }

//    private void initMediaSession() {
//        mMediaSessionManager = (MediaSessionManager) getSystemService(Context.MEDIA_SESSION_SERVICE);
//        ComponentName componentName = new ComponentName(getApplicationContext(), NotificationEventReceiver.class);
//        mMediaSession = new MediaSessionCompat(getApplicationContext(), TAG);
//        mMediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS | MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
//        mMediaSession.setCallback(new MediaSessionCallback());
//    }


    public int getCurrentPodcastIndex() {
        return mCurrentPodcastIndex;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy() called");
        if (mPlayer != null && !mPlayer.isPlaying()) {
            Logger.d("MediaService_onDestroy", "test");
            unregisterReceiver(notificationEventReceiver);
            if (mPlayer != null) {
                mPlayer.release();
                mPlayer = null;
            }
            super.onDestroy();
        }
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
//        stopForeground(true);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return true;
    }

    private void initMediaPlayer() {
        Log.d(TAG, "initMediaPlayer() called");
        mPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mPlayer.setOnPreparedListener(this);
        mPlayer.setOnCompletionListener(this);
        mPlayer.setOnErrorListener(this);
        mPlayer.setOnInfoListener(this);
    }

    public void setTrack(Post pTrack) {
        Log.d(TAG, "setTrack() called with: pTrack = [" + pTrack + "]");
        stopPlayback(false);
        mTrack = pTrack;
        mCurrentPlayType = PlayType.POST;
        startStreaming(true);
    }

    /**
     * Sets podcasts.
     *
     * @param podcasts            list of podcasts
     * @param index               current index of streamed podcast
     * @param shouldRestartStream indicator to restart streaming
     */
    public void setPodcasts(List<Podcast> podcasts, int index, boolean shouldRestartStream) {
        Logger.e(TAG + "_MethodCall", ">>> setPodcasts()");
        if (shouldRestartStream) {
            stopPlayback(false);
        }
        mPodcasts = podcasts;
        mCurrentPlayType = PlayType.PODCAST;
        mCurrentPodcastIndex = index;
        if (shouldRestartStream) {
            startStreaming(false);
        }
    }

    public void addPodcasts(List<Podcast> podcasts) {
        Log.d(TAG, "addPodcasts() called with: podcasts = [" + podcasts + "]");
        List<Podcast> serviceList = new ArrayList<>(getPodcasts());
        serviceList.removeAll(podcasts);
        getPodcasts().addAll(serviceList);
    }

    public List<Podcast> getPodcasts() {
        return mPodcasts;
    }

    @Override
    public void onPrepared(MediaPlayer pMediaPlayer) {
        Log.d(TAG, "onPrepared() called with: pMediaPlayer = [" + pMediaPlayer + "]");
        Logger.e(TAG, ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        Logger.e(TAG, "ON PREPARED, stopped by user: " + mStoppedByUser);
        Logger.e(TAG, ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        if (!mStoppedByUser) {
            mHandler.postDelayed(updateSeekTime, 500);
            pMediaPlayer.start();
            pMediaPlayer.seekTo(0);
            sendBroadcast(new Intent(MyConstants.Media.ACTION_STATUS_PREPARED));
            Logger.e(TAG, ">>> MEDIA PREPARED for " + mCurrentTitle);
            createNotification(mCurrentTitle, true);
            mCurrentDuration = pMediaPlayer.getDuration();
            mIsMediaValid = true;
        }
    }

    public boolean isMediaPlaying() {
        return mPlayer != null && mPlayer.isPlaying();
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
        } else {
            mCurrentPlayType = PlayType.NONE;
        }
    }

    public PlayType getCurrentPlayType() {
        return mCurrentPlayType;
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
                Logger.e(TAG, "MEDIA_INFO_BUFFERING_END");
                sendBroadcast(new Intent(MyConstants.Media.ACTION_MEDIA_BUFFER_STOP));
                break;
            case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                Logger.e(TAG, "MEDIA_INFO_BUFFERING_START");
                sendBroadcast(new Intent(MyConstants.Media.ACTION_MEDIA_BUFFER_START));
                break;
        }
        return true;
    }

    private void playMedia() {
        Log.d(TAG, "playMedia() called");
        try {
            mPlayer.reset();
            if (mCurrentPlayType.equals(PlayType.POST)) {
                setDataSource(mTrack);
            } else {
                Logger.e(TAG, String.format(">>> CHANGING DATA SOURCE (index-%d):\n", mCurrentPodcastIndex)
                        + mPodcasts.get(mCurrentPodcastIndex));
                setDataSource(mPodcasts.get(mCurrentPodcastIndex));
            }
            createNotification(mCurrentTitle, false);
            mPlayer.prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setDataSource(Podcast podcast) throws IOException {
        Log.d(TAG, "setDataSource() called with: podcast = [" + podcast + "]");
        mCurrentTitle = podcast.getTitle();
        mCurrentImage = podcast.getImage();
        mCurrentDescription = podcast.getDescription();
        mCurrentPlayType = PlayType.PODCAST;
        String mediaUrl = podcast.getSource().replace(" ", "%20");
        Logger.e(TAG, ">>> MEDIA URL: " + mediaUrl);
        mPlayer.setDataSource(mediaUrl);
    }

    private void setDataSource(Post pTrack) throws IOException {
        Log.d(TAG, "setDataSource() called with: pTrack = [" + pTrack + "]");
        mCurrentTitle = pTrack.getTitle();
        mCurrentDescription = pTrack.getDescription();
        mCurrentImage = pTrack.getData().getThumbnail();
        mCurrentPlayType = PlayType.POST;
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
        Logger.e(TAG, "mPlayer.isPlaying(): " + mPlayer.isPlaying());
        createNotification(mCurrentTitle, mPlayer.isPlaying());
        sendBroadcast(intent);
    }

    public void stopPlayback(boolean fromPodcastSelection) {
        Log.d(TAG, "stopPlayback() called with: fromPodcastSelection = [" + fromPodcastSelection + "]");
        mCurrentTitle = " ---- ";
        mCurrentImage = "";
        if (!fromPodcastSelection)
            mStoppedByUser = true;
        if (mIsMediaValid)
            mPlayer.stop();

        mCurrentPlayType = PlayType.NONE;
        // clear service podcast list
//        if (mPodcasts != null && !mPodcasts.isEmpty())
//            mPodcasts.clear();

        Intent intent = new Intent(MyConstants.Media.ACTION_PLAY_STATUS_CHANGE);
        intent.putExtra(MyConstants.Extras.KEY_PLAY_STATUS, false);
        sendBroadcast(intent);

        mIsMediaValid = false;
        if (manager != null) {
            Logger.e(TAG, "notification removed");
            stopForeground(true);
        }
    }

    public void showLoadingView() {
        mCurrentTitle = "";
    }

    public void seekTo(int seekbarProgress) {
        int currentPosition = MediaHelper.progressToTimer(seekbarProgress, mPlayer.getDuration());
        mPlayer.seekTo(currentPosition);
    }

    public void startStreaming(boolean shouldRestartPodcastIndex) {
        mStoppedByUser = false;
        if (shouldRestartPodcastIndex)
            mCurrentPodcastIndex = 0;
        Logger.e(TAG, "current podcast index: " + mCurrentPodcastIndex);
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

    public String getCurrentDescription() {
        return mCurrentDescription;
    }

    public String getCurrentImageResource() {
        return mCurrentImage;
    }

    public void changeCurrentPodcast(int index) {
        Log.d(TAG, "changeCurrentPodcast() called with: index = [" + index + "]");
        if (mCurrentPlayType.equals(PlayType.PODCAST)) {
            Logger.e(TAG, ">>> CHANGING PODCAST INDEX to " + index);
            stopPlayback(true);
            mCurrentPodcastIndex = index;
            playMedia();
        }
    }

    private void createNotification(String title, boolean isPlaying) {
        Logger.e(TAG, "============ NOTIFICATION START ==================");
        if (manager == null)
            manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Intent notificationIntent = new Intent(MyConstants.Intent.ACTION_CLICK_PLAYER);
        PendingIntent toOpen = PendingIntent.getBroadcast(getApplicationContext(), NOTIFICATION_ID,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent cancelNotification = new Intent(NotificationEventReceiver.ACTION_DISMISS);
        PendingIntent cancel = PendingIntent
                .getBroadcast(getApplicationContext(), NOTIFICATION_ID, cancelNotification, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent pausePlayNotification = new Intent(NotificationEventReceiver.ACTION_PAUSE_PLAY);
        PendingIntent pausePlay = PendingIntent
                .getBroadcast(getApplicationContext(), NOTIFICATION_ID, pausePlayNotification, PendingIntent.FLAG_UPDATE_CURRENT);

        RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.view_notification);

        contentView.setTextViewText(R.id.notificationTitle, title);

        if (isPlaying) {
            contentView.setImageViewResource(R.id.playpause, R.drawable.ic_pause_notification);
        } else {
            contentView.setImageViewResource(R.id.playpause, R.drawable.ic_play_notification);
        }
        contentView.setOnClickPendingIntent(R.id.playpause, pausePlay);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_logo)
                .setContentInfo("Shuvayatra")
                .setContentIntent(toOpen)
                .setCategory(Notification.CATEGORY_MESSAGE)
                .setVisibility(Notification.VISIBILITY_PUBLIC)
                .setContent(contentView)
                .setDeleteIntent(cancel)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setLights(0x000000FF, 300, 1000);

        Logger.e(TAG, "NOTIFICATION: builder.mActions.size(): " + builder.mActions.size());
        if (isPlaying) {
            startForeground(NOTIFICATION_ID, builder.build());
        } else {
            manager.notify(NOTIFICATION_ID, builder.build());
            stopForeground(false);

        }
    }

    public enum PlayType {
        POST,
        PODCAST,
        NONE
    }

    /*
    To receive events from notification click
     */
    private class NotificationEventReceiver extends BroadcastReceiver {
        private static final String ACTION_DISMISS = "action.NOTIFICATION_DISMISS";
        private static final String ACTION_PAUSE_PLAY = "action.NOTIFICATION_PLAY_PAUSE";

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ACTION_DISMISS)) {
                stopPlayback(false);
                sendBroadcast(new Intent(MyConstants.Media.ACTION_HIDE_MINI_PLAYER));
                // TODO: 1/11/17 close mini player fragment here
                stopForeground(true);
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
