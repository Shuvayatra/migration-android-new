package com.taf.data.repository;

import com.taf.data.repository.datasource.DataStoreFactory;
import com.taf.data.utils.AppPreferences;
import com.taf.model.Post;
import com.taf.repository.IUserAccountRepository;

import java.util.List;
import java.util.Set;

import rx.Observable;

/**
 * Created by umesh on 1/11/17.
 */

public class UserAccountRepository implements IUserAccountRepository {

    AppPreferences mAppPreferences;
    DataStoreFactory mDataStoreFactory;

    public UserAccountRepository(AppPreferences appPreferences, DataStoreFactory dataStoreFactory) {
        mAppPreferences = appPreferences;
        mDataStoreFactory = dataStoreFactory;
    }

    @Override
    public Observable<List<Post>> getFavouritePost() {
        return mDataStoreFactory.createCacheDataStore()
                .getFavouritePosts();
    }
}
