package com.taf.data.repository.datasource;


import javax.inject.Inject;

import database.DaoSession;

/**
 * Created by Nirazan-PC on 12/9/2015.
 */
public class DBDataStore implements IDataStore {

    DaoSession mDaoSession;

    @Inject
    public DBDataStore(DaoSession pDaoSession) {
        mDaoSession = pDaoSession;
    }
}

