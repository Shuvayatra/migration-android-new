package com.taf.shuvayatra.ui.views;

import com.taf.model.ScreenModel;

import java.util.List;

/**
 * Created by umesh on 1/14/17.
 */

public interface HomeActivityView extends LoadDataView {
    void renderScreens(List<ScreenModel> screens);
}
