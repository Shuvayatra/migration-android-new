package com.taf.repository;

import com.taf.model.CountryWidgetData;

import java.util.List;

import rx.Observable;

/**
 * Created by rakeeb on 10/20/16.
 */

public interface IWidgetComponentRepository {
    Observable<CountryWidgetData.Component> getComponent(int type);
}
