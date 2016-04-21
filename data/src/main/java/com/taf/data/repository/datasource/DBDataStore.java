package com.taf.data.repository.datasource;


import android.support.annotation.Nullable;

import com.taf.data.database.DatabaseHelper;
import com.taf.data.database.dao.DbPost;

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
            return mHelper.getPostsPagination(pLimit, pOffset);
        } else {
            return mHelper.getPosts(pLimit, pOffset, pType);
        }
    }

    public Observable<DbPost> getPost(Long pId) {
        return mHelper.getPost(pId);
    }
}

