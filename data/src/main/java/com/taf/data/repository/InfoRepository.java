package com.taf.data.repository;

import com.taf.data.entity.mapper.DataMapper;
import com.taf.data.repository.datasource.DataStoreFactory;
import com.taf.model.Info;
import com.taf.repository.IInfoRepository;

import rx.Observable;
import rx.functions.Action1;

/**
 * Created by yipl on 1/16/17.
 */

public class InfoRepository implements IInfoRepository {
    private final DataStoreFactory mDataStoreFactory;
    private final DataMapper mDataMapper;

    public InfoRepository(DataStoreFactory pDataStoreFactory, DataMapper pDataMapper) {
        mDataMapper = pDataMapper;
        mDataStoreFactory = pDataStoreFactory;
    }

    @Override
    public Observable<Info> getInfo(String key) {
        Observable cacheObservable = mDataStoreFactory.createCacheDataStore()
                .getInfo(key)
                .map(infoEntity -> mDataMapper.transformInfo(infoEntity));
        Observable apiObservable = mDataStoreFactory.createRestDataStore()
                .getInfo(key)
                .map(infoEntity -> mDataMapper.transformInfo(infoEntity));
//        return apiObservable;
        return Observable.concatDelayError(cacheObservable, apiObservable);
    }
}
