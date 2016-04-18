package com.taf.data.repository.datasource;


import com.taf.data.database.dao.DaoSession;

import javax.inject.Inject;

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

