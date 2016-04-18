package com.taf.shuvayatra.di.module;

import android.content.Context;

import com.taf.data.di.PerActivity;
import com.taf.data.entity.mapper.DataMapper;
import com.taf.data.repository.datasource.DataStoreFactory;
import com.taf.util.MyConstants;

import dagger.Module;
import dagger.Provides;
import database.DaoSession;

@Module
public class DataModule {

    private long mId = Long.MIN_VALUE;
    private MyConstants.PostType mPostType = null;
    private MyConstants.Stage mStage = null;
    private MyConstants.DataParent mDataParent = null;
    private int mLimit = -1;
    private int mDownloadStatus = -1;
    private boolean flag = false;

    public DataModule() {
    }

    public DataModule(long pId) {
        mId = pId;
    }

    @Provides
    @PerActivity
    DataStoreFactory provideDataStoreFactory(Context pContext, DataMapper pDataMapper,
                                             DaoSession pDaoSession) {
        return new DataStoreFactory(pContext, pDataMapper, pDaoSession);
    }
}