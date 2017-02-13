package com.taf.shuvayatra.ui.interfaces;


import android.support.v4.app.FragmentManager;

import com.taf.model.ScreenModel;

/**
 *
 */
public interface IDynamicNavigationFragment extends INavigationFragment {

    public void initDynamicNavigation(ScreenModel screenModel, int layout,
                                      FragmentManager fragmentManager);
}
