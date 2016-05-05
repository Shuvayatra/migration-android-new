package com.taf.repository;

import com.taf.model.Post;
import com.taf.model.SyncData;

import java.util.List;

import rx.Observable;

public interface IPostRepository extends IBaseRepository<Post> {
    Observable<List<Post>> getListByType(String pType, int pLimit, int pOffset);

    Observable<List<Post>> getFavouriteList(int pLimit, int pOffset);

    Observable<List<Post>> getPostWithUnSyncedFavourites();

    Observable<Boolean> updateFavouriteState(Long pId, Boolean pStatus);

    Observable<Boolean> syncFavourites(SyncData pSyncData);

    Observable<Boolean> syncFavourites(List<SyncData> pSyncDataList);

    Observable<List<Post>> getPostByCategory(Long pId, int pLimit, int pOffset, String pType,
                                             List<String> excludeTypes, List<Long> excludeIds);

    Observable<List<Post>> getPostWithExcludes(int pLimit, int pOffset, List<String> excludeTypes);

    Observable<Boolean> updateDownloadStatus(long pReference, boolean pDownloadStatus);

    Observable<Boolean> setDownloadReference(long pId, long pReference);

    Observable<Long> updateUnSyncedViewCount(long pId);

    Observable<Long> updateUnSyncedShareCount(long pId);
}
