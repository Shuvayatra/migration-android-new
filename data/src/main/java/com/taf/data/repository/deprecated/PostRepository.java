package com.taf.data.repository.deprecated;

import com.taf.data.entity.SyncDataEntity;
import com.taf.data.entity.mapper.DataMapper;
import com.taf.data.repository.datasource.DataStoreFactory;
import com.taf.model.Post;
import com.taf.model.SyncData;
import com.taf.repository.deprecated.IPostRepository;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

public class PostRepository implements IPostRepository {

    private final DataStoreFactory mDataStoreFactory;
    private final DataMapper mDataMapper;

    public PostRepository(DataStoreFactory pDataStoreFactory, DataMapper pDataMapper) {
        mDataMapper = pDataMapper;
        mDataStoreFactory = pDataStoreFactory;
    }

    @Override
    public Observable<List<Post>> getListByType(String pType, int pLimit, int pOffset) {
        return mDataStoreFactory.createDBDataStore()
                .getPosts(pLimit, pOffset, pType)
                .map(pObjectMap -> mDataMapper.transformPostFromDb(pObjectMap));
    }

    @Override
    public Observable<List<Post>> getSimilarPost(String pType, List<String> pTags, List<Long>
            excludeIds, int pLimit, int pOffset) {
        return mDataStoreFactory.createDBDataStore()
                .getSimilarPosts(pLimit, pOffset, pType, pTags, excludeIds)
                .map(pPosts -> mDataMapper.transformPostFromDb(pPosts));
    }

    @Override
    public Observable<List<Post>> getFavouriteList(int pLimit, int pOffset) {
        return mDataStoreFactory.createDBDataStore()
                .getFavouritePosts(pLimit, pOffset)
                .map(pPosts -> mDataMapper.transformPostFromDb(pPosts));
    }

    @Override
    public Observable<List<Post>> getPostWithUnSyncedFavourites() {
        return mDataStoreFactory.createDBDataStore()
                .getPostWithUnSyncedFavourites()
                .map(pPosts -> mDataMapper.transformPostFromDb(pPosts));
    }

    @Override
    public Observable<Boolean> updateFavouriteState(Long pId, Boolean pStatus) {
        return Observable.just(
                mDataStoreFactory.createDBDataStore()
                        .updateFavouriteState(pId, pStatus) != -1
        );
    }

    @Override
    public Observable<Boolean> syncFavourites(SyncData pSyncData) {
        List<SyncDataEntity> syncList = new ArrayList<>();
        syncList.add(mDataMapper.transformSyncData(pSyncData));
        return mDataStoreFactory.createRestDataStore()
                .syncFavourites(syncList);
    }

    @Override
    public Observable<Boolean> syncFavourites(List<SyncData> pSyncDataList) {
        return mDataStoreFactory.createRestDataStore()
                .syncFavourites(mDataMapper.transformSyncData(pSyncDataList));
    }

    @Override
    public Observable<List<Post>> getPostByCategory(Long pId, int pLimit, int pOffset, String
            pType, List<String> excludeTypes, List<Long> excludeIds) {
        return mDataStoreFactory.createDBDataStore()
                .getPostByCategory(pId, pLimit, pOffset, pType, excludeTypes, excludeIds)
                .map(pObjectMap -> mDataMapper.transformPostFromDb(pObjectMap));
    }

    @Override
    public Observable<List<Post>> getPostWithExcludes(int pLimit, int pOffset, List<String>
            excludeTypes) {
        return mDataStoreFactory.createDBDataStore()
                .getPostWithExcludes(pLimit, pOffset, excludeTypes)
                .map(pObjectMap -> mDataMapper.transformPostFromDb(pObjectMap));
    }

    @Override
    public Observable<Boolean> updateDownloadStatus(long pReference, boolean pDownloadStatus) {
        return Observable.just(
                mDataStoreFactory.createDBDataStore()
                        .updateDownloadStatus(pReference, pDownloadStatus) != -1
        );
    }

    @Override
    public Observable<Boolean> setDownloadReference(long pId, long pReference) {
        return Observable.just(
                mDataStoreFactory.createDBDataStore()
                        .setDownloadReference(pId, pReference) != -1
        );
    }

    @Override
    public Observable<Long> updateUnSyncedViewCount(long pId) {
        return Observable.just(
                mDataStoreFactory.createDBDataStore().updateUnSyncedViewCount(pId)
        );
    }

    @Override
    public Observable<Long> updateUnSyncedShareCount(long pId) {
        return Observable.just(mDataStoreFactory.createDBDataStore().updateUnSyncedShareCount(pId));
    }

    @Override
    public Observable<List<Post>> getPostsByTitle(int pLimit, int pOffset, String title) {
        return mDataStoreFactory.createDBDataStore().getPostsByTitle(pLimit,pOffset,title)
                .map(pObjectMap->mDataMapper.transformPostFromDb(pObjectMap));
    }

    @Override
    public Observable<List<Post>> getPostByTags(int pLimit, int pOffset, List<String> pTags) {
        return mDataStoreFactory.createDBDataStore().getPostsByTags(pLimit,pOffset,pTags)
                .map(pObjectMap->mDataMapper.transformPostFromDb(pObjectMap));
    }

    @Override
    public Observable<List<Post>> getList(int pLimit, int pOffset, String pType, boolean
            pFavouritesOnly, Long pCategoryId, Long pSubCategoryId, List<String> pTags, String
                                                  pSearchQuery, List<Long> pExcludeIds,
                                          List<String> pExcludeTypes) {
        return mDataStoreFactory.createDBDataStore()
                .getPosts(pLimit, pOffset, pType, pFavouritesOnly, pCategoryId, pSubCategoryId,
                        pTags, pSearchQuery, pExcludeIds, pExcludeTypes)
                .map(pPosts -> mDataMapper.transformPostFromDb(pPosts));
    }

    @Override
    public Observable<List<Post>> getList(int pLimit, int pOffset) {
        return mDataStoreFactory.createDBDataStore()
                .getPosts(pLimit, pOffset, null)
                .map(pPosts -> mDataMapper.transformPostFromDb(pPosts));
    }

    @Override
    public Observable<Post> getSingle(Long pId) {
        return mDataStoreFactory.createDBDataStore()
                .getPost(pId)
                .map(pPost -> mDataMapper.transformPostFromDb(pPost, 0));
    }

    /*
    return localDataStore.getPostWithUnSyncedFavourites()
                .map(pPosts -> {
                    Logger.d("PostRepository_syncFavourites", "map post to sync data");
                    return mDataMapper.transformPostForSync(pPosts);
                })
                .doOnNext(pSyncDataList -> {
                    Logger.d("PostRepository_syncFavourites", "sync to server");
                    restDataStore.syncFavourites(pSyncDataList)
                    .doOnNext(pStatus -> {
                        Logger.d("PostRepository_syncFavourites", "respponse from server");
                        status[0] = pStatus;
                    });
                })
                .map(pSyncDataList -> status[0]);
    */
}
