package com.taf.data.repository;

import android.util.Log;

import com.taf.data.entity.mapper.DataMapper;
import com.taf.data.repository.datasource.DataStoreFactory;
import com.taf.data.utils.Utils;
import com.taf.model.Block;
import com.taf.model.base.ApiQueryParams;
import com.taf.repository.IHomeRepository;

import java.util.List;

import rx.Observable;
import rx.functions.Action1;

public class HomeRepository implements IHomeRepository {

    private final DataStoreFactory mDataStoreFactory;
    private final DataMapper mDataMapper;

    public HomeRepository(DataStoreFactory pDataStoreFactory, DataMapper pDataMapper) {
        mDataMapper = pDataMapper;
        mDataStoreFactory = pDataStoreFactory;
    }

    @Override
    public Observable<List<Block>> getBlocks(boolean noCache, ApiQueryParams params) {

        Observable cacheObservable = mDataStoreFactory.createCacheDataStore()
                .getHomeBlocks()
                .map(blockEntities -> mDataMapper.transformBlockEntity(blockEntities))
                .map(blocks -> Utils.sortByPositionBlock(blocks));

        Observable apiObservable = mDataStoreFactory.createRestDataStore()
                .getHomeBlocks(params)
                .map(blockEntities -> mDataMapper.transformBlockEntity(blockEntities))
                .map(blocks -> Utils.sortByPositionBlock(blocks));

        if (noCache) {
            return apiObservable;
        } else {
            return Observable.concatDelayError(cacheObservable, apiObservable);
        }
    }
}
