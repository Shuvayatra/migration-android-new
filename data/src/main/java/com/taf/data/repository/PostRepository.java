package com.taf.data.repository;

import com.taf.data.entity.SyncDataEntity;
import com.taf.data.entity.mapper.DataMapper;
import com.taf.data.repository.datasource.DataStoreFactory;
import com.taf.data.utils.AppPreferences;
import com.taf.model.Post;
import com.taf.model.PostResponse;
import com.taf.repository.IPostRepository;

import java.util.List;

import rx.Observable;

public class PostRepository implements IPostRepository {

    private final DataStoreFactory mDataStoreFactory;
    private final DataMapper mDataMapper;
    private final AppPreferences mPreferences;

    public PostRepository(DataStoreFactory pDataStoreFactory, DataMapper pDataMapper,
                          AppPreferences preferences) {
        mDataMapper = pDataMapper;
        mDataStoreFactory = pDataStoreFactory;
        mPreferences = preferences;
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
                    response.setFromCache(true);
                    return response;
                });

        return Observable.concatDelayError(cacheObservable, apiObservable);
    }

    @Override
    public Observable<Post> getDetail(Long id) {
        Observable apiObservable = mDataStoreFactory.createRestDataStore()
                .getPost(id)
                .map(entity -> mDataMapper.transformPost(entity));

        Observable cacheObservable = mDataStoreFactory.createCacheDataStore()
                .getPost(id)
                .map(entity -> mDataMapper.transformPost(entity));

        return Observable.concatDelayError(cacheObservable, apiObservable);
    }

    @Override
    public Observable<Boolean> updateFavouriteCount(Long id, boolean status) {
        if (status) mPreferences.addToFavourites(id);
        else mPreferences.removeFromFavourites(id);

        return mDataStoreFactory.createRestDataStore()
                .updateFavoriteCount(id, status)
                .map(entity -> entity.getStatus().equals("success"));
    }

    @Override
    public Observable<Boolean> updateShareCount(Long id) {
        return mDataStoreFactory.createRestDataStore()
                .updateShareCount(id)
                .map(entity -> entity.getStatus().equals("success"));
    }

    @Override
    public Observable<Boolean> syncUserActions() {
        return mDataStoreFactory.createDBDataStore()
                .getUnSyncedPosts()
                .map(dbPosts -> mDataMapper.transformUnSyncedData(dbPosts))
                .flatMap(syncDataEntities -> getSyncResponseObservable(syncDataEntities));
    }

    private Observable<Boolean> getSyncResponseObservable(List<SyncDataEntity> data) {
        if (!data.isEmpty()) {
            return mDataStoreFactory.createRestDataStore()
                    .syncUserActions(data);
        } else {
            return Observable.defer(() -> Observable.just(true));
        }
    }

    @Override
    public Observable<PostResponse> getSearchPosts(int limit, int offset, String query, String type){
        return mDataStoreFactory.createRestDataStore()
                .getSearchPosts(limit, offset, query, type)
                .map(postResponseEntity -> mDataMapper.transformPostResponse(postResponseEntity));
    }
}
