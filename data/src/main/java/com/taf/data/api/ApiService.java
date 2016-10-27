package com.taf.data.api;

import com.google.gson.JsonElement;
import com.taf.data.entity.BlockEntity;
import com.taf.data.entity.DeletedContentDataEntity;
import com.taf.data.entity.LatestContentEntity;
import com.taf.data.entity.PodcastEntity;
import com.taf.data.entity.PostResponseEntity;
import com.taf.data.entity.SyncDataEntity;
import com.taf.data.entity.SyncResponseEntity;
import com.taf.model.Country;
import com.taf.util.MyConstants;

import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
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

    @GET(MyConstants.API.HOME)
    Observable<List<BlockEntity>> getHomeBlocks();

    @GET(MyConstants.API.DESTINATION)
    Observable<JsonElement> getCountryList();

    @GET(MyConstants.API.PODCASTS)
    Observable<List<PodcastEntity>> getPodcasts(@Query("channel_id") Long channelId);

    @GET(MyConstants.API.OPEN_WEATHER)
    Observable<JsonElement> getWeatherInfo(@Query("q") String place, @Query("units") String unit, @Query("appid") String appId);

    @GET(MyConstants.API.JOURNEY)
    Observable<List<BlockEntity>> getJourneyContents();

    @GET(MyConstants.API.FOREX)
    Observable<JsonElement> getForex();

    @GET(MyConstants.API.POSTS)
    Observable<PostResponseEntity> getPosts(@Query("per_page") int limit,
                                            @Query("page") int offset,
                                            @Query("category_id") String params);
}
