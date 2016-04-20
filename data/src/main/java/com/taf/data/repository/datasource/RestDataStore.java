package com.taf.data.repository.datasource;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.taf.data.api.ApiRequest;
import com.taf.data.database.DatabaseHelper;
import com.taf.data.entity.LatestContentEntity;
import com.taf.data.exception.NetworkConnectionException;

import rx.Observable;
import rx.schedulers.Schedulers;


public class RestDataStore implements IDataStore {

    private final Context mContext;
    private final ApiRequest mApiRequest;
    private final DatabaseHelper mDBHelper;

    public RestDataStore(Context pContext, ApiRequest pApiRequest, DatabaseHelper pDBHelper) {
        mApiRequest = pApiRequest;
        mDBHelper = pDBHelper;
        mContext = pContext;
    }

    public Observable<LatestContentEntity> getLatestContents(long pLastUpdatedStamp) {
        if (isThereInternetConnection()) {
            return mApiRequest.getLatestContents(pLastUpdatedStamp)
                    .doOnNext(pLatestContentEntity -> {
                        Observable.create(pSubscriber -> {
                            mDBHelper.insertUpdate(pLatestContentEntity);
                            pSubscriber.onCompleted();
                        }).subscribeOn(Schedulers.computation()).subscribe();
                    });
        } else {
            return Observable.error(new NetworkConnectionException());
        }
    }

    //// TODO: 4/20/16
    public Observable updateFavouriteState(Long pId, boolean isFavourite) {
        if (isThereInternetConnection()) {
            return mApiRequest.updateFavouriteState(pId, isFavourite)
                    .doOnNext(pO -> {
                        Observable.create(pSubscriber -> {
                            mDBHelper.updateFavouriteState(pId, isFavourite, true);
                            pSubscriber.onCompleted();
                        }).subscribeOn(Schedulers.computation()).subscribe();
                    })
                    .doOnError(pThrowable -> {
                        Observable.create(pSubscriber -> {
                            mDBHelper.updateFavouriteState(pId, isFavourite, false);
                            pSubscriber.onCompleted();
                        }).subscribeOn(Schedulers.computation()).subscribe();
                    });
        } else {
            Observable.create(pSubscriber -> {
                mDBHelper.updateFavouriteState(pId, isFavourite, false);
                pSubscriber.onCompleted();
            }).subscribeOn(Schedulers.computation()).subscribe();
            return Observable.error(new NetworkConnectionException());
        }
    }

    private boolean isThereInternetConnection() {
        boolean isConnected;

        ConnectivityManager connectivityManager =
                (ConnectivityManager) this.mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        isConnected = (networkInfo != null && networkInfo.isConnectedOrConnecting());

        return isConnected;
    }
}
