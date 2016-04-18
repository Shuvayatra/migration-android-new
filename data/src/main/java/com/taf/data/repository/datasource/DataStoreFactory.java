package com.taf.data.repository.datasource;

import android.content.Context;

import com.taf.data.BuildConfig;
import com.taf.data.database.dao.DaoSession;
import com.taf.data.di.DaggerNetworkComponent;
import com.taf.data.di.NetworkComponent;
import com.taf.data.di.NetworkModule;
import com.taf.data.di.PerActivity;
import com.taf.data.entity.mapper.DataMapper;

import javax.inject.Inject;

@PerActivity
public class DataStoreFactory {
    private final Context mContext;
    private final DataMapper mDataMapper;
    private final DaoSession mDaoSession;

    @Inject
    public DataStoreFactory(Context pContext, DataMapper pDataMapper, DaoSession pDaoSession) {
        mContext = pContext;
        mDataMapper = pDataMapper;
        mDaoSession = pDaoSession;
    }

    public DBDataStore createDBDataStore() {
        return new DBDataStore(mDaoSession);
    }

    public RestDataStore createRestDataStore() {
        NetworkComponent networkComponent = DaggerNetworkComponent
                .builder()
                .networkModule(new NetworkModule(BuildConfig.BASE_URL))
                .build();
        return new RestDataStore(mContext, networkComponent.getApiRequest(), mDaoSession);
    }
}
