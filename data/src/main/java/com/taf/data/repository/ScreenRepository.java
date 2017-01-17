package com.taf.data.repository;

import com.taf.data.entity.mapper.DataMapper;
import com.taf.data.repository.datasource.DataStoreFactory;
import com.taf.model.ScreenDataModel;
import com.taf.model.ScreenModel;
import com.taf.model.base.ApiQueryParams;
import com.taf.repository.IScreenRepository;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import rx.Observable;

/**
 * Created by umesh on 1/13/17.
 */

public class ScreenRepository implements IScreenRepository {

    private DataStoreFactory mDataStoreFactory;
    private DataMapper mDataMapper;

    public ScreenRepository(DataStoreFactory dataStoreFactory, DataMapper dataMapper) {
        mDataStoreFactory = dataStoreFactory;
        mDataMapper = dataMapper;

    }

    @Override
    public Observable<List<ScreenModel>> getScreens(ApiQueryParams params) {
        // TODO: 1/14/17 Url in parameter is only for test. remove the url from parameter.
        Observable apiObservable = mDataStoreFactory.createRestDataStore()
                .getScreenEntity(params)
                .map(screenEntities -> mDataMapper.transformScreenEntity(screenEntities))
                .map(screenModels -> sortByOrder(screenModels));

        Observable cacheObservable = mDataStoreFactory.createCacheDataStore()
                .getScreens()
                .map(screenEntities -> mDataMapper.transformScreenEntity(screenEntities))
                .map(screenModels -> sortByOrder(screenModels));

        return Observable.concatDelayError(cacheObservable, apiObservable);
    }

    @Override
    public Observable<ScreenDataModel> getScreenBlockData(long id, ApiQueryParams params) {
        Observable apiObservable = mDataStoreFactory.createRestDataStore()
                .getScreenBlockData(id, params)
                .map(screenDataEntity -> mDataMapper.transformScreenBlockData(screenDataEntity));

        Observable cacheObservable = mDataStoreFactory.createCacheDataStore()
                .getScreenDataEntity(id)
                .map(screenDataEntity -> {
                    ScreenDataModel dataModel = mDataMapper.transformScreenBlockData(screenDataEntity);
                    dataModel.setFromCache(true);
                    return dataModel;
                });

        return Observable.concatDelayError(cacheObservable, apiObservable);
    }

    @Override
    public Observable<ScreenDataModel> getScreenFeedData(long id, int page, ApiQueryParams params) {
        Observable apiObservable = mDataStoreFactory.createRestDataStore()
                .getScreenFeedData(id, page, params)
                .map(screenDataEntity -> mDataMapper.transformScreenFeedData(screenDataEntity));

        Observable cacheObservable = mDataStoreFactory.createCacheDataStore()
                .getScreenFeedEntity(id)
                .map(screenDataEntity -> {
                    ScreenDataModel dataModel = mDataMapper.transformScreenFeedData(screenDataEntity);
                    dataModel.setFromCache(true);
                    return dataModel;
                });

        return Observable.concatDelayError(cacheObservable, apiObservable);
    }

    public List<ScreenModel> sortByOrder(List<ScreenModel> screens) {
        Comparator<ScreenModel> comparator = new Comparator<ScreenModel>() {
            @Override
            public int compare(ScreenModel o1, ScreenModel o2) {
                return o1.getOrder() <= o2.getOrder() ? -1 : 1;
            }
        };

        Collections.sort(screens, comparator);
        return screens;
    }

}
