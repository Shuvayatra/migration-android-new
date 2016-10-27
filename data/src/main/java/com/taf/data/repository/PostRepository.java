package com.taf.data.repository;

import com.taf.data.entity.mapper.DataMapper;
import com.taf.data.repository.datasource.DataStoreFactory;
import com.taf.model.PostResponse;
import com.taf.repository.IPostRepository;

import rx.Observable;

public class PostRepository implements IPostRepository {

    private final DataStoreFactory mDataStoreFactory;
    private final DataMapper mDataMapper;

    public PostRepository(DataStoreFactory pDataStoreFactory, DataMapper pDataMapper) {
        mDataMapper = pDataMapper;
        mDataStoreFactory = pDataStoreFactory;
    }

    @Override
    public Observable<PostResponse> getList(int limit, int offset, String filterParams) {
        Observable apiObservable = mDataStoreFactory.createRestDataStore()
                .getPosts(limit, offset, filterParams)
                .map(responseEntity -> mDataMapper.transformPostResponse(responseEntity));

        Observable cacheObservable = mDataStoreFactory.createCacheDataStore()
                .getPosts(filterParams)
                .map(entities -> {
                    PostResponse response = new PostResponse(1, 1);
                    response.setData(mDataMapper.transformPost(entities));
                    response.setTotal(entities.size());
                    return response;
                });

        return Observable.concatDelayError(cacheObservable, apiObservable);
    }
}
