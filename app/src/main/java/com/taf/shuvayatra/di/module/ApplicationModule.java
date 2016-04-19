package com.taf.shuvayatra.di.module;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.taf.data.database.MyOpenHelper;
import com.taf.data.database.dao.DaoMaster;
import com.taf.data.database.dao.DaoSession;
import com.taf.data.executor.JobExecutor;
import com.taf.executor.PostExecutionThread;
import com.taf.executor.ThreadExecutor;
import com.taf.shuvayatra.MyApplication;
import com.taf.shuvayatra.util.AppPreferences;
import com.taf.shuvayatra.util.UIThread;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {
    private final MyApplication mApplication;

    public ApplicationModule(MyApplication pApplication) {
        this.mApplication = pApplication;
    }

    @Provides
    @Singleton
    Context provideApplicationContext() {
        return this.mApplication;
    }

    @Provides
    @Singleton
    ThreadExecutor provideThreadExecutor(JobExecutor jobExecutor) {
        return jobExecutor;
    }

    @Provides
    @Singleton
    PostExecutionThread providePostExecutionThread(UIThread uiThread) {
        return uiThread;
    }

    @Provides
    @Singleton
    DaoSession provideDaoSession(Context pContext) {
        DaoMaster.DevOpenHelper helper = new MyOpenHelper(pContext, "shuvayatraDB", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        return daoMaster.newSession();
    }

    @Provides
    @Singleton
    AppPreferences provideAppPreferences(Context pContext) {
        return new AppPreferences(pContext);
    }
}
