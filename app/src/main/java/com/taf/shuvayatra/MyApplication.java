package com.taf.shuvayatra;

import android.app.Application;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.stetho.Stetho;
import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.PeriodicTask;
import com.google.android.gms.gcm.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.taf.data.utils.Logger;
import com.taf.shuvayatra.di.component.ApplicationComponent;
import com.taf.shuvayatra.di.component.DaggerApplicationComponent;
import com.taf.shuvayatra.di.module.ApplicationModule;
import com.taf.shuvayatra.media.MediaService;
import com.taf.shuvayatra.service.MyTaskService;
import com.taf.shuvayatra.util.AnalyticsUtil;
import com.taf.shuvayatra.util.Utils;
import com.taf.util.MyConstants;

public class MyApplication extends Application {
    private static final String TAG = "MyApplication";

    public MediaService mService;
    ApplicationComponent mApplicationComponent;
    private GcmNetworkManager mGcmNetworkManager;
    public static boolean isActivityShowing;

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.e(TAG, "Application on create called");

        this.mApplicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();

        Fresco.initialize(this);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this);
        }

        if (!BuildConfig.DEBUG) {
            FirebaseMessaging.getInstance().subscribeToTopic("global");
        } else {
            FirebaseMessaging.getInstance().subscribeToTopic("test");
        }

        AnalyticsUtil.logAppOpenEvent(mApplicationComponent.getAnalytics());
        scheduleSyncTask();
    }

    private void scheduleSyncTask() {
        mGcmNetworkManager = GcmNetworkManager.getInstance(this);

        PeriodicTask mySyncTask = new PeriodicTask.Builder()
                .setService(MyTaskService.class)
                .setTag(MyTaskService.TAG_POST_FAV_SHARE)
                .setPeriod(4 * 60 * 60)
                .setFlex(20)
                .setRequiredNetwork(Task.NETWORK_STATE_CONNECTED)
                .setPersisted(true)
                .build();

        mGcmNetworkManager.schedule(mySyncTask);
    }


    public ApplicationComponent getApplicationComponent() {
        return mApplicationComponent;
    }

}
