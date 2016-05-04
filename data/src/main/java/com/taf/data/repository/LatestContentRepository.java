package com.taf.data.repository;

import com.taf.data.entity.mapper.DataMapper;
import com.taf.data.repository.datasource.DataStoreFactory;
import com.taf.data.repository.datasource.RestDataStore;
import com.taf.model.LatestContent;
import com.taf.repository.IBaseRepository;

import java.util.List;

import rx.Observable;

public class LatestContentRepository implements IBaseRepository<LatestContent> {

    private final DataStoreFactory mDataStoreFactory;
    private final DataMapper mDataMapper;

    public LatestContentRepository(DataStoreFactory pDataStoreFactory, DataMapper pDataMapper) {
        mDataMapper = pDataMapper;
        mDataStoreFactory = pDataStoreFactory;
    }

    @Override
    public Observable<List<LatestContent>> getList(int pLimit, int pOffset) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Observable<LatestContent> getSingle(Long pLastUpdateStamp) {
        RestDataStore restDataStore = mDataStoreFactory.createRestDataStore();
        return restDataStore.getLatestContents(pLastUpdateStamp == -1 ? null : pLastUpdateStamp)
                .map(pLatestContentEntity -> mDataMapper.transformLatestContent
                        (pLatestContentEntity));
    }
}
