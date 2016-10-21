package com.taf.shuvayatra.di.component;


import android.content.Context;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.taf.data.database.dao.DaoSession;
import com.taf.executor.PostExecutionThread;
import com.taf.executor.ThreadExecutor;
import com.taf.shuvayatra.base.BaseActivity;
import com.taf.shuvayatra.di.module.ApplicationModule;
import com.taf.data.utils.AppPreferences;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
    void inject(BaseActivity pBaseActivity);
    //void inject(MediaService pMediaService);

    Context getContext();

    ThreadExecutor threadExecutor();

    PostExecutionThread postExecutionThread();

    AppPreferences getAppPreferences();

    DaoSession getDaoSession();

    FirebaseAnalytics getAnalytics();
}
