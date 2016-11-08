package com.taf.shuvayatra.base;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;

import com.taf.shuvayatra.MyApplication;
import com.taf.shuvayatra.media.MediaService;

/**
 * Created by julian on 11/4/16.
 */

public abstract class MediaServiceActivity extends PlayerFragmentActivity {

    public Intent mPlayIntent;
    public boolean mMusicBound = false;

    // connect to the service
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MediaService.MusicBinder binder = (MediaService.MusicBinder) service;
            ((MyApplication) getApplicationContext()).mService = binder.getService();
            mMusicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mMusicBound = false;
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPlayIntent = new Intent(this, MediaService.class);
        bindService(mPlayIntent, mConnection, Context.BIND_AUTO_CREATE);
        startService(mPlayIntent);
    }

    @Override
    protected void onDestroy() {
        ((MyApplication) getApplicationContext()).mService.stopPlayback();
        // Unbind from the service
        if (mMusicBound) {
            unbindService(mConnection);
            mMusicBound = false;
        }

        stopService(mPlayIntent);
        ((MyApplication) getApplicationContext()).mService = null;
        super.onDestroy();
    }
}
