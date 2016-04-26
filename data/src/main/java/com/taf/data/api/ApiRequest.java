package com.taf.data.api;

import com.taf.data.entity.LatestContentEntity;
import com.taf.data.entity.SyncDataEntity;
import com.taf.data.entity.SyncResponseEntity;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

public class ApiRequest {
    ApiService mApiService;

    @Inject
    public ApiRequest(ApiService pApiService) {
        mApiService = pApiService;
    }

    public Observable<LatestContentEntity> getLatestContents(long pLatestUpdateStamp) {
        return mApiService.getLatestContent(pLatestUpdateStamp);
    }

    public Observable<SyncResponseEntity> updateFavouriteState(List<SyncDataEntity> pSyncDataList){
        return mApiService.syncLikes(pSyncDataList);
    }
}
