package com.taf.data.api;

import com.google.gson.JsonElement;
import com.taf.data.BuildConfig;
import com.taf.data.entity.BlockEntity;
import com.taf.data.entity.ChannelEntity;
import com.taf.data.entity.CountryEntity;
import com.taf.data.entity.DeletedContentDataEntity;
import com.taf.data.entity.LatestContentEntity;
import com.taf.data.entity.PodcastResponseEntity;
import com.taf.data.entity.PostEntity;
import com.taf.data.entity.PostResponseEntity;
import com.taf.data.entity.SyncDataEntity;
import com.taf.data.entity.SyncResponseEntity;
import com.taf.data.entity.UpdateRequestEntity;
import com.taf.data.entity.UpdateResponseEntity;
import com.taf.data.exception.NetworkConnectionException;
import com.taf.data.utils.Logger;
import com.taf.model.CountryWidgetData;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;

public class ApiRequest {
    public static final String TAG = "ApiRequest";
    ApiService mApiService;

    @Inject
    public ApiRequest(ApiService pApiService) {
        mApiService = pApiService;
    }

    public Observable<LatestContentEntity> getLatestContents(Long pLatestUpdateStamp) {
        return mApiService.getLatestContent(pLatestUpdateStamp);
    }

    public Observable<DeletedContentDataEntity> getDeletedContent(Long pLatestUpdateStamp) {
        return mApiService.getDeletedContent(pLatestUpdateStamp);
    }

    public Observable<SyncResponseEntity> updateFavouriteState(List<SyncDataEntity> pSyncDataList) {
        return mApiService.syncLikes(pSyncDataList);
    }

    public Observable<List<BlockEntity>> getHomeBlocks() {
        return mApiService.getHomeBlocks();
    }

    public Observable<PodcastResponseEntity> getPodcasts(Long channelId) {
        return mApiService.getPodcasts(channelId);
    }

    public Observable<PostResponseEntity> getPosts(int limit, int offset, String params) {
        return mApiService.getPosts(limit, offset, params);
    }

    public Observable<PostEntity> getPost(Long id) {
        return mApiService.getPost(id);
    }

    public Observable<UpdateResponseEntity> updateFavoriteCount(Long id, boolean status) {
        return mApiService.updateFavouriteCount(id,
                new UpdateRequestEntity(status ? "up" : "down"));
    }

    public Observable<UpdateResponseEntity> updateShareCount(Long id) {
        return mApiService.updateShareCount(id);
    }

    public Observable<CountryWidgetData.Component> getComponent(int componentType) {
        switch (componentType) {
            case CountryWidgetData.COMPONENT_FOREX:
                return Observable.error(new NetworkConnectionException());
            case CountryWidgetData.COMPONENT_WEATHER:
                return Observable.error(new NetworkConnectionException());
        }
        return null;
    }

    public Observable<JsonElement> getWeather(String place, String unit) {
        return mApiService.getWeatherInfo(place, unit, BuildConfig.OPEN_WEATHER_APPID);
    }

    public Observable<List<CountryEntity>> getCountryList() {
        return mApiService.getCountryList();
    }

    public Observable<List<BlockEntity>> getJourneyContent() {
        return mApiService.getJourneyContents();
    }

    public Observable<JsonElement> getForex() {
        return mApiService.getForex();
    }

    public Observable<List<BlockEntity>> getDestinationBlock(long id) {
        return mApiService.getDestinationBlocks(id);
    }

    public Observable<SyncResponseEntity> syncUserAtions(List<SyncDataEntity> pSyncDataList) {
        return mApiService.syncUserActions(pSyncDataList);
    }

    public Observable<List<ChannelEntity>> getChannelList(){
        return mApiService.getChannelList();
    }

    public Observable<PostResponseEntity> getSearchPosts(int limit, int offset, String query, String type){
        return mApiService.getSearchPosts(limit, offset, query, type);
    }
}
