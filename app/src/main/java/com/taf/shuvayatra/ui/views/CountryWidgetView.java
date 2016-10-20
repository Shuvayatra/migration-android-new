package com.taf.shuvayatra.ui.views;


import com.taf.model.CountryWidgetData;
import com.taf.model.CountryWidgetData.Component;
import com.taf.shuvayatra.ui.deprecated.interfaces.LoadDataView;

/**
 * Corresponding view interface for {@link CountryWidgetData}
 * Contains loading, error and loaded view along with {@link Component#componentType()}
 *
 * @see CountryWidgetData
 */

public interface CountryWidgetView extends LoadDataView {

    void onComponentLoaded(Component component);

    void onLoadingView(int type);

    void onHideLoadingView(int type);

    void onErrorView(int type, String error);
}
