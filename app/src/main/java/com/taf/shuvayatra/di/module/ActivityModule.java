package com.taf.shuvayatra.di.module;

import android.app.Activity;

import com.taf.data.di.PerActivity;

import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {
    private final Activity mActivity;

    public ActivityModule() {
        mActivity = null;
    }

    public ActivityModule(Activity pActivity) {
        this.mActivity = pActivity;
    }

    @Provides
    @PerActivity
    Activity provideActivity() {
        return mActivity;
    }
}


