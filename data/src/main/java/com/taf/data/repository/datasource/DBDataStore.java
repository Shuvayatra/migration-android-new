package com.taf.data.repository.datasource;


import android.support.annotation.Nullable;

import com.taf.data.database.DatabaseHelper;
import com.taf.data.database.dao.DbCategory;
import com.taf.data.database.dao.DbNotification;
import com.taf.data.database.dao.DbPost;
import com.taf.data.database.dao.DbTag;
import com.taf.model.Notification;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Observable;

public class DBDataStore implements IDataStore {

    DatabaseHelper mHelper;

    @Inject
    public DBDataStore(DatabaseHelper pHelper) {
        mHelper = pHelper;
    }

    public Observable<Map<String, Object>> getPosts(int pLimit, int pOffset, @Nullable String
            pType) {
        if (pType == null) {
            return mHelper.getPosts(pLimit, pOffset);
        } else {
            return mHelper.getPostsByType(pLimit, pOffset, pType);
        }
    }

    public Observable<Map<String, Object>> getSimilarPosts(int pLimit, int pOffset, @Nullable String
            pType, List<String> pTags, List<Long> pExcludeIds) {
        return mHelper.getSimilarPosts(pLimit, pOffset, pType, pTags, pExcludeIds);
    }

    public Long updateFavouriteState(Long pId, boolean isFavourite) {
        return mHelper.updateFavouriteState(pId, isFavourite, false);
    }

    public Observable<List<DbPost>> getPostWithUnSyncedFavourites() {
        return mHelper.getPostsWithUnSyncedFavourites();
    }

    public Observable<Map<String, Object>> getFavouritePosts(int pLimit, int pOffset) {
        return mHelper.getFavouritePosts(pLimit, pOffset);
    }

    public Observable<DbPost> getPost(Long pId) {
        return mHelper.getPost(pId);
    }

    public Observable<List<DbCategory>> getCategoriesBySection(String sectionName, boolean
            isCategory, Long pParentId) {
        return mHelper.getCategoriesBySection(sectionName, isCategory, pParentId);
    }

    public Observable<Map<String, Object>> getPostByCategory(Long pId, int pLimit, int pOffset,
                                                             String pType, List<String>
                                                                     excludeTypes, List<Long>
                                                                     excludeIds) {
        return mHelper.getPostByCategory(pId, pLimit, pOffset, pType, excludeTypes, excludeIds);
    }

    public Observable<Map<String, Object>> getPostWithExcludes(int pLimit, int pOffset,
                                                               List<String> excludeTypes) {
        return mHelper.getPostWithExcludes(pLimit, pOffset, excludeTypes);
    }

    public long updateDownloadStatus(Long pReference, boolean pDownloadStatus) {
        return mHelper.updateDownloadStatus(pReference, pDownloadStatus);
    }

    public long setDownloadReference(Long pId, long pReference) {
        return mHelper.setDownloadReference(pId, pReference);
    }

    public Observable<List<DbNotification>> getNotifications() {
        return mHelper.getNotifications();
    }

    public Observable<Boolean> saveNotification(Notification pNotification) {
        return mHelper.saveNotification(pNotification);
    }

    public long updateUnSyncedViewCount(Long pId){
        return mHelper.updateUnSyncedViewCount(pId);
    }

    public long updateUnSyncedShareCount(Long pId){
        return mHelper.updateUnSyncedShareCount(pId);
    }

    public Observable<List<DbTag>> getTags() {
        return mHelper.getTags();
    }
}

