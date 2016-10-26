package com.taf.shuvayatra.ui.views;

import com.taf.model.Country;
import com.taf.shuvayatra.ui.deprecated.interfaces.LoadDataView;

import java.util.List;

/**
 * Created by rakeeb on 10/25/16.
 */

public interface CountryView extends LoadDataView {

    void renderCountries(List<Country> countryList);
}
