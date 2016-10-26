package com.taf.data.repository;

import com.taf.data.BuildConfig;
import com.taf.data.entity.mapper.DataMapper;
import com.taf.data.repository.datasource.DataStoreFactory;
import com.taf.data.utils.AppPreferences;
import com.taf.model.Country;
import com.taf.repository.ICountryRepository;

import java.util.List;

import rx.Observable;

/**
 * Created by rakeeb on 10/25/16.
 */

public class CountryRepository implements ICountryRepository {

    private final DataStoreFactory mDataStoreFactory;
    private final DataMapper mDataMapper;

    private static final String TAG = "CountryRepository";

    public CountryRepository(DataStoreFactory factory, DataMapper mapper) {

        mDataStoreFactory = factory;
        mDataMapper = mapper;
    }


    @Override
    public Observable<List<Country>> getCountryList() {
        return mDataStoreFactory.createRestDataStore(BuildConfig.SHUVAYATRA_BASE_URL).getCountryList().map(
                jsonElement -> {
                    return mDataMapper.transformCountryList(jsonElement);
                }
        );
    }
}
