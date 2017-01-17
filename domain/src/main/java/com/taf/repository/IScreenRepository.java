package com.taf.repository;

import com.taf.model.ScreenDataModel;
import com.taf.model.ScreenModel;
import com.taf.model.base.ApiQueryParams;

import java.util.List;

import rx.Observable;

/**
 * Created by umesh on 1/13/17.
 */

public interface IScreenRepository {
    Observable<List<ScreenModel>> getScreens(ApiQueryParams params);

    Observable<ScreenDataModel> getScreenBlockData(long id, ApiQueryParams params);

    Observable<ScreenDataModel> getScreenFeedData(long id, int page, ApiQueryParams params);
}
