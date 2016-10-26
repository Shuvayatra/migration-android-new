package com.taf.repository;

import com.taf.model.Country;

import java.util.List;

import rx.Observable;

/**
 * Created by rakeeb on 10/25/16.
 */

public interface ICountryRepository {

    Observable<List<Country>> getCountryList();
}
