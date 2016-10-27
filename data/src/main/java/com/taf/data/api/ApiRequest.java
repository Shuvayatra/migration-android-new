package com.taf.data.api;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.taf.data.BuildConfig;
import com.taf.data.entity.BlockEntity;
import com.taf.data.entity.DeletedContentDataEntity;
import com.taf.data.entity.LatestContentEntity;
import com.taf.data.entity.PodcastEntity;
import com.taf.data.entity.SyncDataEntity;
import com.taf.data.entity.SyncResponseEntity;
import com.taf.data.exception.NetworkConnectionException;
import com.taf.data.utils.Logger;
import com.taf.model.CountryWidgetData;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

public class ApiRequest {
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
        Logger.e("ApiRequest", "send request: " + new Gson().toJson(pSyncDataList));
        return mApiService.syncLikes(pSyncDataList);
    }

    public Observable<List<BlockEntity>> getHomeBlocks() {
        return mApiService.getHomeBlocks();
    }

    public Observable<List<PodcastEntity>> getPodcasts(Long channelId) {
        return mApiService.getPodcasts(channelId);
    }

    public Observable<CountryWidgetData.Component> getComponent(int componentType) {
        // todo add api request for each component
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

    public Observable<JsonElement> getCountryList() {
        return mApiService.getCountryList();
    }

    public Observable<List<BlockEntity>> getJourneyContent() {
        return mApiService.getJourneyContents();
    }

    public Observable<JsonElement> getForex() {
        return mApiService.getForex();
    }
}
