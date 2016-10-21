package com.taf.data.repository.datasource;

import android.content.Context;

import com.taf.data.BuildConfig;
import com.taf.data.database.DatabaseHelper;
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
    private final DatabaseHelper mHelper;

    @Inject
    public DataStoreFactory(Context pContext, DataMapper pDataMapper, DatabaseHelper
            pHelper) {
        mContext = pContext;
        mDataMapper = pDataMapper;
        mHelper = pHelper;
    }

    public DBDataStore createDBDataStore() {
        return new DBDataStore(mHelper);
    }

    public RestDataStore createRestDataStore() {
        NetworkComponent networkComponent = DaggerNetworkComponent
                .builder()
                .networkModule(new NetworkModule(BuildConfig.BASE_URL))
                .build();
        return new RestDataStore(mContext, networkComponent.getApiRequest(), mHelper);
    }

    public RestDataStore createRestDataStore(String baseUrl) {
        NetworkComponent networkComponent = DaggerNetworkComponent
                .builder()
                .networkModule(new NetworkModule(baseUrl))
                .build();
        return new RestDataStore(mContext, networkComponent.getApiRequest(), mHelper);
    }
}
