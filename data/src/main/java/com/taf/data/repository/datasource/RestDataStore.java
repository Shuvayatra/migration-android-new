package com.taf.data.repository.datasource;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.gson.JsonElement;
import com.taf.data.api.ApiRequest;
import com.taf.data.cache.CacheImpl;
import com.taf.data.database.DatabaseHelper;
import com.taf.data.entity.BlockEntity;
import com.taf.data.entity.CountryEntity;
import com.taf.data.entity.DeletedContentDataEntity;
import com.taf.data.entity.LatestContentEntity;
import com.taf.data.entity.PodcastEntity;
import com.taf.data.entity.PostEntity;
import com.taf.data.entity.PostResponseEntity;
import com.taf.data.entity.SyncDataEntity;
import com.taf.data.entity.UpdateResponseEntity;
import com.taf.data.exception.NetworkConnectionException;
import com.taf.data.utils.Logger;
import com.taf.model.CountryWidgetData;

import java.util.List;

import rx.Observable;
import rx.schedulers.Schedulers;


public class RestDataStore implements IDataStore {

    public static final String TAG = "RestDataStore";
    private final Context mContext;
    private final ApiRequest mApiRequest;
    private final DatabaseHelper mDBHelper;
    private final CacheImpl mCache;


    public RestDataStore(Context pContext,
                         ApiRequest pApiRequest,
                         DatabaseHelper pDBHelper,
                         CacheImpl cache) {
        mApiRequest = pApiRequest;
        mDBHelper = pDBHelper;
        mContext = pContext;
        mCache = cache;
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
                        mCache.saveHomeBlocks(blockEntities);
                    });
        } else {
            return Observable.error(new NetworkConnectionException());
        }
    }

    public Observable<List<PodcastEntity>> getPodcasts(Long channelId) {
        if (isThereInternetConnection()) {
            return mApiRequest.getPodcasts(channelId)
                    .doOnNext(entities -> {
                        mCache.savePodcastsByChannelId(entities, channelId);
                    });
        } else {
            return Observable.error(new NetworkConnectionException());
        }
    }

    public Observable<PostResponseEntity> getPosts(int limit, int offset, String filterParams) {
        if (isThereInternetConnection()) {
            return mApiRequest.getPosts(limit, offset, filterParams)
                    .doOnNext(responseEntity -> {
                        mCache.savePosts(filterParams, responseEntity.getData(), (offset != 1));
                    });
        } else {
            return Observable.error(new NetworkConnectionException());
        }
    }

    public Observable<PostEntity> getPost(Long id) {
        if (isThereInternetConnection()) {
            return mApiRequest.getPost(id)
                    .doOnNext(entity -> {
                        mCache.savePost(entity);
                    });
        } else {
            return Observable.error(new NetworkConnectionException());
        }
    }

    public Observable<UpdateResponseEntity> updateFavoriteCount(Long id, boolean status) {
        if (isThereInternetConnection()) {
            return mApiRequest.updateFavoriteCount(id, status)
                    .doOnNext(entity -> {
                        if (!entity.getStatus().equals("success")) {
                            mDBHelper.updateUnSyncedPost(id, status, false);
                        }
                    });
        } else {
            mDBHelper.updateUnSyncedPost(id, status, false);
            return Observable.error(new NetworkConnectionException());
        }
    }

    public Observable<UpdateResponseEntity> updateShareCount(Long id) {
        if (isThereInternetConnection()) {
            return mApiRequest.updateShareCount(id)
                    .doOnNext(entity -> {
                        if (!entity.getStatus().equals("success")) {
                            mDBHelper.updateUnSyncedPost(id, null, true);
                        }
                    });
        } else {
            mDBHelper.updateUnSyncedPost(id, null, true);
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

    public Observable<List<CountryEntity>> getCountryList() {
        if (isThereInternetConnection()) {
            return mApiRequest.getCountryList().doOnNext(
                    countryEntities -> {
                        mCache.saveCountryList(countryEntities);
                    }
            );
        } else {
            return Observable.error(new NetworkConnectionException());
        }
    }

    public Observable<List<BlockEntity>> getJourneyContents() {
        if (isThereInternetConnection()) {
            return mApiRequest.getJourneyContent()
                    .doOnNext(blockEntities -> {
                        mCache.saveJourneyBlocks(blockEntities);
                    });
        } else {
            return Observable.error(new NetworkConnectionException());
        }
    }

    public Observable<JsonElement> getForexInfo() {
        if (isThereInternetConnection())
            return mApiRequest.getForex();
        else
            return Observable.error(new NetworkConnectionException());
    }

    public Observable<List<BlockEntity>> getDestinationBlocks(long id) {
        if (isThereInternetConnection()) {
            return mApiRequest.getDestinationBlock(id)
                    .doOnNext(blockEntities -> {
                        mCache.saveDestinationBlocks(id, blockEntities);
                    });
        } else {
            return Observable.error(new NetworkConnectionException());
        }
    }

    public Observable<Boolean> syncUserActions(List<SyncDataEntity> entities) {
        if (isThereInternetConnection()) {
            return mApiRequest.syncUserAtions(entities)
                    .doOnNext(responseEntity -> {
                        Observable.create(pSubscriber -> {
                            mDBHelper.deleteSyncedPosts(responseEntity.getSuccessIdList());
                            pSubscriber.onCompleted();
                        }).subscribeOn(Schedulers.computation()).subscribe();
                    })
                    .map(responseEntity -> responseEntity.getFailedIdList().isEmpty());
        } else {
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
