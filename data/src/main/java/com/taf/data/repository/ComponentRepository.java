package com.taf.data.repository;


import com.taf.data.BuildConfig;
import com.taf.data.entity.mapper.DataMapper;
import com.taf.data.repository.datasource.DataStoreFactory;
import com.taf.data.utils.AppPreferences;
import com.taf.data.utils.DateUtils;
import com.taf.data.utils.Logger;
import com.taf.interactor.UseCaseData;
import com.taf.model.CountryWidgetData;
import com.taf.repository.IWidgetComponentRepository;

import java.util.Calendar;

import rx.Observable;

/**
 * Created by rakeeb on 10/20/16.
 */

public class ComponentRepository implements IWidgetComponentRepository {

    private final DataStoreFactory mDataStoreFactory;
    private final DataMapper mDataMapper;
    AppPreferences mAppPreferences;
    String unit = "metric";

    private static final String TAG = "ComponentRepository";

    public ComponentRepository(DataStoreFactory factory, DataMapper mapper, AppPreferences appPreferences) {
        mDataStoreFactory = factory;
        mDataMapper = mapper;
        mAppPreferences = appPreferences;
    }

    @Override
    public Observable<CountryWidgetData.Component> getComponent(int type, UseCaseData data) {
        switch (type) {
            case CountryWidgetData.COMPONENT_CALENDAR:
                Logger.e(TAG, ">>> component calendar");
                CountryWidgetData.CalendarComponent component = new CountryWidgetData.CalendarComponent();
                Calendar today = data.getSerializable(UseCaseData.CALENDAR_INSTANCE) == null ? Calendar.getInstance() :
                        (Calendar) data.getSerializable(UseCaseData.CALENDAR_INSTANCE);
                String nepaliDate = new DateUtils.NepaliDateConverter().englishToNepali(today);
                component.setToday(today);
                component.setNepaliDate(nepaliDate);

                return Observable.just(component);
            case CountryWidgetData.COMPONENT_FOREX:
                // create base url here
                long lastForexUpdatedTime = mAppPreferences.getLastForexUpdatedTime();
                Logger.e(TAG,"component lastForexUpdatedTime: "+ lastForexUpdatedTime);
                if(lastForexUpdatedTime == Long.MIN_VALUE ||
                        (lastForexUpdatedTime + android.text.format.DateUtils.HOUR_IN_MILLIS * 2)< System.currentTimeMillis()) {
                    return mDataStoreFactory.createRestDataStore(BuildConfig.HAMRO_PATRO_URL)
                            .getForexInfo()
                            .doOnNext(jsonElement -> {
                                mAppPreferences.saveLastForexUpdate(System.currentTimeMillis());
                            })
                            .map(jsonElement -> mDataMapper.transformForexInfo(jsonElement));
                } else {
                    return mDataStoreFactory.createCacheDataStore()
                            .getForex()
                            .map(jsonElement -> mDataMapper.transformForexInfo(jsonElement));
                }

            case CountryWidgetData.COMPONENT_WEATHER:

                long lastWeatherUpdatedTime = mAppPreferences.getLastWeatherUpdatedTime();
                Logger.e(TAG,"component lastWeatherUpdatedTime: "+ lastWeatherUpdatedTime);
                if(lastWeatherUpdatedTime == Long.MIN_VALUE ||
                        (lastWeatherUpdatedTime + android.text.format.DateUtils.HOUR_IN_MILLIS * 2)< System.currentTimeMillis()) {

                    String location = data.getString(UseCaseData.COUNTRY_CODE);
                    return mDataStoreFactory.createRestDataStore(BuildConfig.OPEN_WEATHER_URL)
                            .getWeatherInfo(location, unit)
                            .doOnNext(jsonElement -> mAppPreferences.saveLastWeatherUpdate(System.currentTimeMillis()))
                            .map(jsonElement -> mDataMapper.transformWeatherInfo(jsonElement));
                } else {
                    return mDataStoreFactory.createCacheDataStore()
                            .getWeather()
                            .map(jsonElement -> mDataMapper.transformWeatherInfo(jsonElement));
                }
        }
        return null;
    }
}
