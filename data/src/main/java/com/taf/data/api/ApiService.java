package com.taf.data.api;

import com.taf.data.entity.DeletedContentDataEntity;
import com.taf.data.entity.LatestContentEntity;
import com.taf.data.entity.SyncDataEntity;
import com.taf.data.entity.SyncResponseEntity;
import com.taf.util.MyConstants;

import java.util.List;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;
import rx.Observable;

public interface ApiService {
    @GET(MyConstants.API.LATEST_CONTENT)
    Observable<LatestContentEntity> getLatestContent(@Query("last_updated") Long pLastUpdateStamp);

    @GET(MyConstants.API.DELETED_CONTENT)
    Observable<DeletedContentDataEntity> getDeletedContent(@Query("last_updated") Long
                                                               pLastUpdateStamp);

    @POST(MyConstants.API.SYNC_DATA)
    Observable<SyncResponseEntity> syncLikes(@Body List<SyncDataEntity> pSyncDataList);
}
