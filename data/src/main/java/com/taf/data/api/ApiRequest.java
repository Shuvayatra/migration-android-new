package com.taf.data.api;

import com.taf.data.entity.LatestContentEntity;

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

    // TODO: 4/20/16
    public Observable updateFavouriteState(Long pId, boolean isFavourite){
        //return mApiService.
        return null;
    }
}
