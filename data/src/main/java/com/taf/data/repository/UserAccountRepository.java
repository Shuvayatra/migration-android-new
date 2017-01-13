package com.taf.data.repository;

import android.content.Context;
import android.text.TextUtils;

import com.taf.data.entity.mapper.DataMapper;
import com.taf.data.repository.datasource.DataStoreFactory;
import com.taf.data.utils.AppPreferences;
import com.taf.model.Post;
import com.taf.model.UserInfoModel;
import com.taf.repository.IUserAccountRepository;
import com.taf.util.MyConstants;

import java.util.List;

import rx.Observable;

/**
 * Created by umesh on 1/11/17.
 */

public class UserAccountRepository implements IUserAccountRepository {

    DataStoreFactory mDataStoreFactory;
    DataMapper mDataMapper;

    public UserAccountRepository(
                                 DataStoreFactory dataStoreFactory,
                                 DataMapper dataMapper) {
        mDataStoreFactory = dataStoreFactory;
        mDataMapper = dataMapper;

    }

    @Override
    public Observable<List<Post>> getFavouritePost() {
        return mDataStoreFactory.createCacheDataStore()
                .getFavouritePosts();
    }

    @Override
    public Observable<Boolean> saveUserInfo(UserInfoModel userInfoModel) {

        return mDataStoreFactory.createRestDataStore()
                .saveUserInfo(mDataMapper.transformUserInfo(userInfoModel))
                .map(response-> response.isStatus());

    }
}
