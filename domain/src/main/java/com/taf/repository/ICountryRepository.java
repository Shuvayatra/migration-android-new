package com.taf.repository;

import com.taf.model.Block;
import com.taf.model.Country;
import com.taf.model.base.ApiQueryParams;

import java.util.List;

import rx.Observable;

/**
 * Created by rakeeb on 10/25/16.
 */

public interface ICountryRepository {

    Observable<List<Country>> getCountryList(boolean useCache);
    Observable<List<Block>> getCountryBlocks(long id, ApiQueryParams params);
}
