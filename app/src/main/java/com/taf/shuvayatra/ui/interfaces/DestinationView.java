package com.taf.shuvayatra.ui.interfaces;

import com.taf.model.Category;

import java.util.List;

public interface DestinationView extends LoadDataView {
    public void renderCountries(List<Category> pCountries);
}
