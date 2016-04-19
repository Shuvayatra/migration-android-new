package com.taf.data.repository.datasource;


import com.taf.data.database.DatabaseHelper;

import javax.inject.Inject;

/**
 * Created by Nirazan-PC on 12/9/2015.
 */
public class DBDataStore implements IDataStore {

    DatabaseHelper mHelper;

    @Inject
    public DBDataStore(DatabaseHelper pHelper) {
        mHelper = pHelper;
    }
}

