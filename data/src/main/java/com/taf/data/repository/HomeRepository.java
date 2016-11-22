package com.taf.data.repository;

import com.taf.data.entity.mapper.DataMapper;
import com.taf.data.repository.datasource.DataStoreFactory;
import com.taf.model.Block;
import com.taf.repository.IHomeRepository;

import java.util.List;

import rx.Observable;

public class HomeRepository implements IHomeRepository {

    private final DataStoreFactory mDataStoreFactory;
    private final DataMapper mDataMapper;

    public HomeRepository(DataStoreFactory pDataStoreFactory, DataMapper pDataMapper) {
        mDataMapper = pDataMapper;
        mDataStoreFactory = pDataStoreFactory;
    }

    @Override
    public Observable<List<Block>> getBlocks(boolean noCache) {

        Observable cacheObservable = mDataStoreFactory.createCacheDataStore()
                .getHomeBlocks()
                .map(blockEntities -> mDataMapper.transformBlockEntity(blockEntities));

        Observable apiObservable = mDataStoreFactory.createRestDataStore()
                .getHomeBlocks()
                .map(blockEntities -> mDataMapper.transformBlockEntity(blockEntities));

        if(noCache) {
            return apiObservable;
        }else{
            return Observable.concatDelayError(cacheObservable, apiObservable);
        }
    }
}
