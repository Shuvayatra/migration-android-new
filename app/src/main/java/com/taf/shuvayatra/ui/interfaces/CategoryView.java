package com.taf.shuvayatra.ui.interfaces;

import com.taf.model.Category;

import java.util.List;

/**
 * Created by Nirazan-PC on 4/21/2016.
 */
public interface CategoryView extends LoadDataView {
    public void renderCategories(List<Category> pCategories);
}
