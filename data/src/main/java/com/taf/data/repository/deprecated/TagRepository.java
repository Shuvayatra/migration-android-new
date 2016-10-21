package com.taf.data.repository.deprecated;

import com.taf.data.entity.mapper.DataMapper;
import com.taf.data.repository.datasource.DataStoreFactory;
import com.taf.repository.deprecated.ITagRepository;

import java.util.List;

import rx.Observable;

public class TagRepository implements ITagRepository {

    private final DataStoreFactory mDataStoreFactory;
    private final DataMapper mDataMapper;

    public TagRepository(DataStoreFactory pDataStoreFactory, DataMapper pDataMapper) {
        mDataMapper = pDataMapper;
        mDataStoreFactory = pDataStoreFactory;
    }

    @Override
    public Observable<List<String>> getList(int pLimit, int pOffset) {
        return mDataStoreFactory.createDBDataStore()
                .getTags()
                .map(pDbTags -> mDataMapper.transformTags(pDbTags));
    }

    @Override
    public Observable<String> getSingle(Long pId) {
        throw new UnsupportedOperationException();
    }
}
