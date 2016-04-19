package com.taf.data.api;

import com.taf.data.entity.LatestContentEntity;
import com.taf.util.MyConstants;

import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

public interface ApiService {
    @GET(MyConstants.API.LATEST_CONTENT)
    Observable<LatestContentEntity> getLatestContent(@Query("last_updated") long pLastUpdateStamp);
}
