package com.taf.shuvayatra;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.stetho.Stetho;
import com.google.firebase.messaging.FirebaseMessaging;
import com.taf.shuvayatra.di.component.ApplicationComponent;
import com.taf.shuvayatra.di.component.DaggerApplicationComponent;
import com.taf.shuvayatra.di.module.ApplicationModule;
import com.taf.shuvayatra.util.AnalyticsUtil;

public class MyApplication extends Application {
    ApplicationComponent mApplicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        this.mApplicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
        Fresco.initialize(this);
        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this);
        }

        if (!BuildConfig.DEBUG) {
            FirebaseMessaging.getInstance().subscribeToTopic("global");
        } else {
            FirebaseMessaging.getInstance().subscribeToTopic("test");
        }

        AnalyticsUtil.logAppOpenEvent(mApplicationComponent.getAnalytics());
    }

    public ApplicationComponent getApplicationComponent() {
        return mApplicationComponent;
    }
}
