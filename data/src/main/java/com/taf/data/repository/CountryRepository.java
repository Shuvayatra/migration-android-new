package com.taf.data.repository;

import com.taf.data.entity.mapper.DataMapper;
import com.taf.data.repository.datasource.DataStoreFactory;
import com.taf.model.Block;
import com.taf.model.Country;
import com.taf.model.base.ApiQueryParams;
import com.taf.repository.ICountryRepository;

import java.util.List;

import rx.Observable;

/**
 * Created by rakeeb on 10/25/16.
 */

public class CountryRepository implements ICountryRepository {

    private final DataStoreFactory mDataStoreFactory;
    private final DataMapper mDataMapper;

    private static final String TAG = "CountryRepository";

    public CountryRepository(DataStoreFactory factory, DataMapper mapper) {

        mDataStoreFactory = factory;
        mDataMapper = mapper;
    }


    @Override
    public Observable<List<Country>> getCountryList() {

        Observable cacheObservable = mDataStoreFactory.createCacheDataStore()
                .getCountryList()
                .map(countryEntities -> mDataMapper.transformCountryList(countryEntities));

        Observable apiObservable = mDataStoreFactory.createRestDataStore().getCountryList()
                .map(countryEntities -> mDataMapper.transformCountryList(countryEntities));

        return Observable.concatDelayError(cacheObservable, apiObservable);
    }


    @Override
    public Observable<List<Country>> getCachedCountryList() {
        return mDataStoreFactory.createCacheDataStore()
                .getCountryList()
                .map(countryEntities -> mDataMapper.transformCountryList(countryEntities));
    }

    @Override
    public Observable<List<Block>> getCountryBlocks(long id) {
        Observable<List<Block>> cacheObservable = mDataStoreFactory.createCacheDataStore()
                .getDestinationBlocks(id)
                .map(blockEntities -> mDataMapper.transformBlockEntity(blockEntities));
        Observable<List<Block>> apiObservable = mDataStoreFactory.createRestDataStore()
                .getDestinationBlocks(id)
                .map(blockEntities -> mDataMapper.transformBlockEntity(blockEntities));

        return Observable.concatDelayError(cacheObservable, apiObservable);
    }
}
