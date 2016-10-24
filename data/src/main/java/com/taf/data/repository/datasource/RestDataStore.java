package com.taf.data.repository.datasource;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.gson.JsonElement;
import com.taf.data.api.ApiRequest;
import com.taf.data.database.DatabaseHelper;
import com.taf.data.entity.BlockEntity;
import com.taf.data.entity.DeletedContentDataEntity;
import com.taf.data.entity.LatestContentEntity;
import com.taf.data.entity.SyncDataEntity;
import com.taf.data.exception.NetworkConnectionException;
import com.taf.data.utils.Logger;
import com.taf.model.CountryWidgetData;

import java.util.List;

import rx.Observable;
import rx.functions.Action1;
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

    public Observable<LatestContentEntity> getLatestContents(Long pLastUpdatedStamp) {
        if (isThereInternetConnection()) {
            return mApiRequest.getLatestContents(pLastUpdatedStamp)
                    .doOnNext(pLatestContentEntity -> {
                        Logger.d("RestDataStore_getLatestContents", "insert/update");
                        Observable.create(pSubscriber -> {
                            mDBHelper.insertUpdate(pLatestContentEntity);
                            Logger.e("RestDataStore", "completed insert");
                            pSubscriber.onCompleted();
                        }).subscribe();
                    });
        } else {
            return Observable.error(new NetworkConnectionException());
        }
    }

    public Observable<DeletedContentDataEntity> getDeletedContent(long pLastUpdatedStamp) {
        if (isThereInternetConnection()) {
            return mApiRequest.getDeletedContent(pLastUpdatedStamp)
                    .doOnNext(pDeletedContentDataEntity -> mDBHelper.deleteContents
                            (pDeletedContentDataEntity));
        } else {
            return Observable.error(new NetworkConnectionException());
        }
    }

    public Observable<Boolean> syncFavourites(List<SyncDataEntity> pSyncData) {
        if (isThereInternetConnection()) {
            return mApiRequest.updateFavouriteState(pSyncData)
                    .doOnNext(pResponseEntity -> {
                        Observable.create(pSubscriber -> {
                            mDBHelper.updateFavouriteState(pResponseEntity.getSuccessIdList(),
                                    true);
                            mDBHelper.updateFavouriteState(pResponseEntity.getFailedIdList(),
                                    false);
                            pSubscriber.onCompleted();
                        }).subscribeOn(Schedulers.computation()).subscribe();
                    })
                    .map(pResponseEntity -> true);
        } else {
            return Observable.error(new NetworkConnectionException());
        }
    }

    public Observable<List<BlockEntity>> getHomeBlocks() {
        if (isThereInternetConnection()) {
            return mApiRequest.getHomeBlocks()
                    .doOnNext(blockEntities -> {
                        // TODO: 10/18/16 save to cache
                    });
        } else {
            return Observable.error(new NetworkConnectionException());
        }
    }

    public Observable<CountryWidgetData.Component> getComponent(int componentType) {
        if (isThereInternetConnection()) {
            return mApiRequest.getComponent(componentType);
        } else {
            return Observable.error(new NetworkConnectionException());
        }
    }
    public Observable<JsonElement> getWeatherInfo(String place, String unit) {
        if (isThereInternetConnection()) {
            return mApiRequest.getWeather(place, unit);
        } else {
            return Observable.error(new NetworkConnectionException());
        }
    }
    public Observable<List<BlockEntity>> getJourneyContents(){
        if(isThereInternetConnection()){
            return mApiRequest.getJourneyContent()
                    .doOnNext(blockEntities -> {
                        // // TODO: 10/21/16 save offline cache
                    });
        }else{
            return Observable.error(new NetworkConnectionException());
        }
    }

    public Observable<JsonElement> getForexInfo() {
        if (isThereInternetConnection())
            return mApiRequest.getForex();
        else
            return Observable.error(new NetworkConnectionException());
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
