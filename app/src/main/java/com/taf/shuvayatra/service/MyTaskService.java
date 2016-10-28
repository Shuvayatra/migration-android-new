package com.taf.shuvayatra.service;

import android.os.Looper;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.GcmTaskService;
import com.google.android.gms.gcm.TaskParams;
import com.taf.data.utils.Logger;
import com.taf.shuvayatra.MyApplication;
import com.taf.shuvayatra.di.component.DaggerDataComponent;
import com.taf.shuvayatra.di.module.ActivityModule;
import com.taf.shuvayatra.di.module.DataModule;
import com.taf.shuvayatra.presenter.UserActionsSyncPresenter;

import javax.inject.Inject;

public class MyTaskService extends GcmTaskService {

    public static final String TAG_POST_FAV_SHARE = "sync-post-fav-share";

    @Inject
    UserActionsSyncPresenter mPresenter;

    @Override
    public void onCreate() {
        super.onCreate();
        initialize();
    }

    private void initialize() {
        DaggerDataComponent.builder()
                .dataModule(new DataModule())
                .activityModule(new ActivityModule())
                .applicationComponent(((MyApplication) getApplicationContext())
                        .getApplicationComponent())
                .build()
                .inject(this);
    }

    @Override
    public int onRunTask(TaskParams pTaskParams) {
        Looper.prepare();
        initialize();

        String tag = pTaskParams.getTag();
        Logger.d("MyTaskService_onRunTask", "test task tag: " + tag);
        if (tag.equals(TAG_POST_FAV_SHARE)) {
            mPresenter.initialize(null);
            return GcmNetworkManager.RESULT_SUCCESS;
        }
        return GcmNetworkManager.RESULT_FAILURE;
    }
}
