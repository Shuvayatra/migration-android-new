package com.taf.repository;

import com.taf.model.ScreenDataModel;
import com.taf.model.ScreenModel;

import java.util.List;

import rx.Observable;

/**
 * Created by umesh on 1/13/17.
 */

public interface IScreenRepository {
    Observable<List<ScreenModel>> getScreens();
    Observable<ScreenDataModel> getScreenBlockData(long id, String endPoint);
    Observable<ScreenDataModel> getScreenFeedData(long id,int page, String endPoint);
}
