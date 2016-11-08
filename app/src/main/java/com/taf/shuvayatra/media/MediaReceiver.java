package com.taf.shuvayatra.media;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.taf.shuvayatra.ui.views.AudioPlayerView;
import com.taf.util.MyConstants;

public class MediaReceiver extends BroadcastReceiver {

    AudioPlayerView mListener = null;

    public MediaReceiver(AudioPlayerView pListener) {
        mListener = pListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(MyConstants.Media.ACTION_MEDIA_BUFFER_START)) {
            mListener.onBufferStarted();
        } else if (intent.getAction().equals(MyConstants.Media.ACTION_MEDIA_BUFFER_STOP)) {
            mListener.onBufferStopped();
        } else if (intent.getAction().equals(MyConstants.Media.ACTION_STATUS_PREPARED)) {
            mListener.onMediaPrepared();
        } else if (intent.getAction().equals(MyConstants.Media.ACTION_PLAY_STATUS_CHANGE)) {
            mListener.playStatusChanged(intent.getBooleanExtra(MyConstants.Extras
                    .KEY_PLAY_STATUS, false));
        } else if (intent.getAction().equals(MyConstants.Media.ACTION_MEDIA_COMPLETE)) {
            mListener.onMediaComplete();
        } else if (intent.getAction().equals(MyConstants.Media.ACTION_PROGRESS_CHANGE)) {
            long[] lengths = intent.getLongArrayExtra("lengths");
            if (lengths == null) {
                mListener.onMediaProgressReset();
            } else {
                mListener.onMediaProgressChanged(lengths);
            }
        }
    }
}
