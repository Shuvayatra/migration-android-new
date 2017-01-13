package com.taf.data.repository;

import com.taf.data.entity.mapper.DataMapper;
import com.taf.data.repository.datasource.DataStoreFactory;
import com.taf.model.PostResponse;
import com.taf.repository.INewsRepository;

import java.util.List;

import rx.Observable;
@Deprecated
public class NewsRepository implements INewsRepository {

    private final DataStoreFactory mDataStoreFactory;
    private final DataMapper mDataMapper;

    public NewsRepository(DataStoreFactory pDataStoreFactory, DataMapper pDataMapper) {
        mDataMapper = pDataMapper;
        mDataStoreFactory = pDataStoreFactory;
    }

    @Override
    public Observable<PostResponse> getNewsList(boolean noCache, int page) {
        return null;
    }

    //    @Override
//    public Observable<PostResponse> getNewsList(boolean noCache, int page) {
//        Observable cacheObservable = mDataStoreFactory.createCacheDataStore()
//                .getNewsList()
//                .map(blockEntities -> mDataMapper.transformPostResponse(blockEntities));
//
//        Observable apiObservable = mDataStoreFactory.createRestDataStore()
//                .getNewsList(page)
//                .map(blockEntities -> {
//                    PostResponse response = new PostResponse(1, 1);
//                    response.setData(mDataMapper.transformPost(blockEntities.getData()));
//                    response.setTotal(blockEntities.getData().size());
//                    response.setFromCache(true);
//                    return response;
//                });
//
//        return Observable.concatDelayError(cacheObservable, apiObservable);
//    }
}
