package com.taf.data.repository;

import com.taf.data.entity.mapper.DataMapper;
import com.taf.data.repository.datasource.DataStoreFactory;
import com.taf.model.Block;
import com.taf.repository.IJourneyRepository;

import java.util.List;

import rx.Observable;

public class JourneyRepository implements IJourneyRepository {

    DataStoreFactory mDataStoreFactory;
    DataMapper mDataMapper;

    public JourneyRepository(DataStoreFactory dataStoreFactory,DataMapper dataMapper){
        mDataStoreFactory = dataStoreFactory;
        mDataMapper = dataMapper;
    }

    @Override
    public Observable<List<Block>> getBlocks() {
        return mDataStoreFactory.createRestDataStore()
                .getJourneyContents()
                .map(contents -> mDataMapper.transformBlockEntity(contents));
    }
}
