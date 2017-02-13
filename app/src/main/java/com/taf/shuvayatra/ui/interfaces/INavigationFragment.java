package com.taf.shuvayatra.ui.interfaces;

import android.support.v4.app.FragmentManager;

import com.taf.shuvayatra.ui.activity.HomeActivity;

/**
 * used in {@link HomeActivity#showFragment(int)}
 */
public interface INavigationFragment {

    public void initNavigation(int layout, FragmentManager fragmentManager);
    public String screenName();
}
