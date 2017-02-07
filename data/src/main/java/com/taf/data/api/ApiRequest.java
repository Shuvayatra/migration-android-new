package com.taf.data.api;

import com.google.gson.JsonElement;
import com.taf.data.BuildConfig;
import com.taf.data.entity.BlockEntity;
import com.taf.data.entity.ChannelEntity;
import com.taf.data.entity.CountryEntity;
import com.taf.data.entity.DeletedContentDataEntity;
import com.taf.data.entity.InfoEntity;
import com.taf.data.entity.LatestContentEntity;
import com.taf.data.entity.PodcastResponseEntity;
import com.taf.data.entity.PostEntity;
import com.taf.data.entity.PostResponseEntity;
import com.taf.data.entity.ScreenBlockEntity;
import com.taf.data.entity.ScreenEntity;
import com.taf.data.entity.ScreenFeedEntity;
import com.taf.data.entity.SyncDataEntity;
import com.taf.data.entity.SyncResponseEntity;
import com.taf.data.entity.UpdateRequestEntity;
import com.taf.data.entity.UpdateResponseEntity;
import com.taf.data.entity.UserInfoEntity;
import com.taf.data.entity.UserInfoResponse;
import com.taf.data.exception.NetworkConnectionException;
import com.taf.model.CountryWidgetData;
import com.taf.model.base.ApiQueryParams;
import com.taf.util.MyConstants;

import java.util.List;

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

    public Observable<List<BlockEntity>> getHomeBlocks(ApiQueryParams params) {
        return mApiService.getHomeBlocks(params.countryId, params.gender);
    }

    public Observable<List<BlockEntity>> getJourneyContent(ApiQueryParams params) {
        return mApiService.getJourneyContents(params.countryId, params.gender);
    }

    public Observable<List<ScreenEntity>> getScreens(ApiQueryParams params) {
        return mApiService.getScreenEntity(params.countryId, params.gender);
    }

    public Observable<ScreenBlockEntity> getScreenBlockData(long id, ApiQueryParams params) {
        return mApiService.getScreenBlockData(id, params.countryId, params.gender);
    }

    public Observable<ScreenFeedEntity> getScreenFeedData(long id, int page, ApiQueryParams params) {
        return mApiService.getScreenFeedData(id, page, params.countryId, params.gender);
    }

    public Observable<PodcastResponseEntity> getPodcasts(int offset, Long channelId) {
        return mApiService.getPodcasts(offset, channelId);
    }

    public Observable<PostResponseEntity> getPosts(int limit, int offset, String params, long id) {
        return mApiService.getPosts(limit, offset, params, id);
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

    public Observable<JsonElement> getForex() {
        return mApiService.getForex();
    }

    public Observable<List<BlockEntity>> getDestinationBlock(long id, ApiQueryParams params) {
        return mApiService.getDestinationBlocks(id, params.countryId, params.gender);
    }

    public Observable<SyncResponseEntity> syncUserAtions(List<SyncDataEntity> pSyncDataList) {
        return mApiService.syncUserActions(pSyncDataList);
    }

    public Observable<List<ChannelEntity>> getChannelList() {
        return mApiService.getChannelList();
    }

    public Observable<PostResponseEntity> getSearchPosts(int limit, int offset, String query, String type) {
        return mApiService.getSearchPosts(limit, offset, query, type);
    }

    public Observable<PostResponseEntity> getNewsList(int page, int offset) {
        return mApiService.getNewsList(page, offset);
    }

    public Observable<UserInfoResponse> saveUserInfo(UserInfoEntity entity) {
        return mApiService.saveUserInfo(entity);
    }

    public Observable<InfoEntity> getInfo(int type) {
        switch (type) {
            case MyConstants.Adapter.TYPE_ABOUT:
                return mApiService.getAbout();
            case MyConstants.Adapter.TYPE_CONTACT_US:
                return mApiService.getContactUs();
            default:
                return Observable.empty();
        }
    }

}
