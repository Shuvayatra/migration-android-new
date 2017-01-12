package com.taf.data.repository;

import com.taf.data.entity.mapper.DataMapper;
import com.taf.data.repository.datasource.DataStoreFactory;
import com.taf.model.Block;
import com.taf.repository.IHomeRepository;
import com.taf.repository.INewsRepository;

import java.util.List;

import rx.Observable;

public class NewsRepository implements INewsRepository {

    private final DataStoreFactory mDataStoreFactory;
    private final DataMapper mDataMapper;

    public NewsRepository(DataStoreFactory pDataStoreFactory, DataMapper pDataMapper) {
        mDataMapper = pDataMapper;
        mDataStoreFactory = pDataStoreFactory;
    }

    @Override
    public Observable<List<Block>> getBlocks(boolean noCache) {

        Observable cacheObservable = mDataStoreFactory.createCacheDataStore()
                .getHomeBlocks()
                .map(blockEntities -> mDataMapper.transformBlockEntity(blockEntities));

        Observable apiObservable = mDataStoreFactory.createRestDataStore()
                .getNewsBlocks()
                .map(blockEntities -> mDataMapper.transformBlockEntity(blockEntities));

        if (noCache) {
            return apiObservable;
        } else {
            return Observable.concatDelayError(cacheObservable, apiObservable);
        }
    }
}
