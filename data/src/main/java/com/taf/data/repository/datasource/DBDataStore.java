package com.taf.data.repository.datasource;


import android.support.annotation.Nullable;

import com.taf.data.database.DatabaseHelper;
import com.taf.data.database.dao.DbPost;
import com.taf.data.database.dao.DbCategory;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

public class DBDataStore implements IDataStore {

    DatabaseHelper mHelper;

    @Inject
    public DBDataStore(DatabaseHelper pHelper) {
        mHelper = pHelper;
    }

    public Observable<List<DbPost>> getPosts(int pLimit, int pOffset, @Nullable String pType) {
        if (pType == null) {
            return mHelper.getPosts(pLimit, pOffset);
        } else {
            return mHelper.getPostsByType(pLimit, pOffset, pType);
        }
    }

    public Observable<List<DbPost>> getFavouritePosts(int pLimit, int pOffset) {
        return mHelper.getFavouritePosts(pLimit, pOffset);
    }

    public Observable<DbPost> getPost(Long pId) {
        return mHelper.getPost(pId);
    }
    public Observable<List<DbCategory>> getCategoriesBySection(String sectionName){
        return mHelper.getCategoriesBySection(sectionName);
    }

    public Observable<List<DbPost>> getPostByCategory(Long pId, int pLimit, int pOffset){
        return mHelper.getPostByCategory(pId, pLimit, pOffset);
    }
}

