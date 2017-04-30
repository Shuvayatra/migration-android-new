package com.taf.data.repository.datasource;

import android.content.Context;

import com.taf.data.BuildConfig;
import com.taf.data.cache.CacheImpl;
import com.taf.data.database.DatabaseHelper;
import com.taf.data.di.DaggerNetworkComponent;
import com.taf.data.di.NetworkComponent;
import com.taf.data.di.NetworkModule;
import com.taf.data.di.PerActivity;
import com.taf.data.entity.mapper.DataMapper;
import com.taf.data.utils.Logger;

@PerActivity
public class DataStoreFactory {
    private static final String TAG="DataStoreFactory";
    private final Context mContext;
    private final DataMapper mDataMapper;
    private final DatabaseHelper mHelper;
    CacheImpl mCache;

    public DataStoreFactory(Context pContext,
                            DataMapper pDataMapper, DatabaseHelper pHelper, CacheImpl cache) {
        mContext = pContext;
        mDataMapper = pDataMapper;
        mHelper = pHelper;
        mCache = cache;
    }

    public DBDataStore createDBDataStore() {
        return new DBDataStore(mHelper);
    }

    public RestDataStore createRestDataStore() {
        NetworkComponent networkComponent = DaggerNetworkComponent
                .builder()
                .networkModule(new NetworkModule(BuildConfig.BASE_URL))
                .build();
        return new RestDataStore(mContext,
                networkComponent.getApiRequest(),
                mHelper, mCache);
    }

    public RestDataStore createRestDataStore(String baseUrl) {
        Logger.i(TAG,"base url = "+ baseUrl);

        NetworkComponent networkComponent = DaggerNetworkComponent
                .builder()
                .networkModule(new NetworkModule(baseUrl))
                .build();
        return new RestDataStore(mContext,
                networkComponent.getApiRequest(),
                mHelper, mCache);
    }

    public CacheDataStore createCacheDataStore() {
        return new CacheDataStore(mCache);
    }
}
