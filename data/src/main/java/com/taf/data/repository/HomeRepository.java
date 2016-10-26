package com.taf.data.repository;

import com.taf.data.entity.mapper.DataMapper;
import com.taf.data.repository.datasource.DataStoreFactory;
import com.taf.model.Block;
import com.taf.repository.IHomeRepository;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class HomeRepository implements IHomeRepository {

    private final DataStoreFactory mDataStoreFactory;
    private final DataMapper mDataMapper;

    public HomeRepository(DataStoreFactory pDataStoreFactory, DataMapper pDataMapper) {
        mDataMapper = pDataMapper;
        mDataStoreFactory = pDataStoreFactory;
    }

    @Override
    public Observable<List<Block>> getBlocks() {

        Observable cacheObservable = mDataStoreFactory.createCacheDataStore()
                .getHomeBlocks()
                .map(blockEntities -> mDataMapper.transformBlockEntity(blockEntities));

        Observable apiObservable = mDataStoreFactory.createRestDataStore()
                .getHomeBlocks()
                .map(blockEntities -> mDataMapper.transformBlockEntity(blockEntities));

        return Observable.concatDelayError(cacheObservable, apiObservable);
    }
}
