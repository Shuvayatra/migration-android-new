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
    public Observable<List<Block>> getBlocks() {
        Observable apiObservable = mDataStoreFactory.createRestDataStore()
                .getHomeBlocks()
                .map(blockEntities -> mDataMapper.transformBlockEntity(blockEntities));

        /*Observable cacheObservable = mDataStoreFactory.createCacheDataStore()
                .getHomeBlocks()
                .map(blockEntities -> mDataMapper.transformBlockEntity(blockEntities));

        return Observable.concat(cacheObservable, apiObservable);*/

        return apiObservable;
    }
}
