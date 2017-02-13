package com.taf.data.api;

import com.google.gson.JsonElement;
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
import com.taf.util.MyConstants;

import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

public interface ApiService {
    @GET(MyConstants.API.LATEST_CONTENT)
    Observable<LatestContentEntity> getLatestContent(@Query("last_updated") Long pLastUpdateStamp);

    @GET(MyConstants.API.DELETED_CONTENT)
    Observable<DeletedContentDataEntity> getDeletedContent(@Query("last_updated") Long
                                                                   pLastUpdateStamp);

    @POST(MyConstants.API.SYNC_DATA)
    Observable<SyncResponseEntity> syncLikes(@Body List<SyncDataEntity> pSyncDataList);

    @GET(MyConstants.API.SCREEN)
    Observable<List<ScreenEntity>> getScreenEntity(@Query("country_id") String countryId,
                                                   @Query("gender") String gender);

    @GET(MyConstants.API.HOME)
    Observable<List<BlockEntity>> getHomeBlocks(@Query("country_id") String countryId,
                                                @Query("gender") String gender);

    @GET(MyConstants.API.JOURNEY)
    Observable<List<BlockEntity>> getJourneyContents(@Query("country_id") String countryId,
                                                     @Query("gender") String gender);

    @GET(MyConstants.API.SCREEN_DATA)
    Observable<ScreenBlockEntity> getScreenBlockData(@Path("id") long id,
                                                     @Query("country_id") String countryId,
                                                     @Query("gender") String gender);

    @GET(MyConstants.API.SCREEN_DATA)
    Observable<ScreenFeedEntity> getScreenFeedData(@Path("id") long id,
                                                   @Query("page") int page,
                                                   @Query("country_id") String countryId,
                                                   @Query("gender") String gender);

    @GET(MyConstants.API.DESTINATION)
    Observable<List<CountryEntity>> getCountryList();

    @GET(MyConstants.API.PODCASTS)
    Observable<PodcastResponseEntity> getPodcasts(@Query("page") int offset,
                                                  @Query("category") Long channelId);

    @GET(MyConstants.API.OPEN_WEATHER)
    Observable<JsonElement> getWeatherInfo(@Query("q") String place,
                                           @Query("units") String unit,
                                           @Query("appid") String appId);

    @GET(MyConstants.API.FOREX)
    Observable<JsonElement> getForex();

    @GET(MyConstants.API.POSTS)
    Observable<PostResponseEntity> getPosts(@Query("per_page") int limit,
                                            @Query("page") int offset,
                                            @Query("category_id") String params,
                                            @Query("country_id") String countryId,
                                            @Query("block_id") String blockId);

    @GET(MyConstants.API.POST)
    Observable<PostEntity> getPost(@Path("id") Long id);

    @POST(MyConstants.API.POST_FAVOURITE)
    Observable<UpdateResponseEntity> updateFavouriteCount(@Path("id") Long id, @Body
            UpdateRequestEntity requestEntity);

    @POST(MyConstants.API.POST_SHARE)
    Observable<UpdateResponseEntity> updateShareCount(@Path("id") Long id);

    @GET(MyConstants.API.DESTINATION_DETAIL)
    Observable<List<BlockEntity>> getDestinationBlocks(@Path("id") long id,
                                                       @Query("country_id") String countryId,
                                                       @Query("gender") String gender);

    @POST(MyConstants.API.SYNC_DATA)
    Observable<SyncResponseEntity> syncUserActions(@Body List<SyncDataEntity> pSyncDataList);

    @GET(MyConstants.API.CHANNEL)
    Observable<List<ChannelEntity>> getChannelList();

    @GET(MyConstants.API.SEARCH_POSTS)
    Observable<PostResponseEntity> getSearchPosts(@Query("per_page") int limit,
                                                  @Query("page") int offset,
                                                  @Query("query") String query,
                                                  @Query("post_type") String type);

    @GET(MyConstants.API.NEWS)
    Observable<PostResponseEntity> getNewsList(@Query("per_page") int limit,
                                               @Query("page") int offset);

    @POST(MyConstants.API.ON_BOARDING_PUSH)
    Observable<UserInfoResponse> saveUserInfo(@Body UserInfoEntity userInfoEntity);

    @GET(MyConstants.API.ABOUT)
    Observable<InfoEntity> getAbout();

    @GET(MyConstants.API.CONTACT_US)
    Observable<InfoEntity> getContactUs();
}
