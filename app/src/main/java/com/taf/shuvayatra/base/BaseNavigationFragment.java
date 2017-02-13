package com.taf.shuvayatra.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.taf.shuvayatra.base.BaseFragment;
import com.taf.shuvayatra.ui.interfaces.INavigationFragment;
import com.taf.shuvayatra.util.AnalyticsUtil;

/**
 *
 */

public abstract class BaseNavigationFragment extends BaseFragment implements INavigationFragment {

    public abstract Fragment defaultInstance();

    public abstract String fragmentTag();

    @Override
    public void initNavigation(int layout, FragmentManager fragmentManager) {
        Fragment fragment = fragmentManager.findFragmentByTag(fragmentTag()) == null ?
                defaultInstance() : fragmentManager.findFragmentByTag(fragmentTag());
        fragmentManager.beginTransaction().replace(layout, fragment, fragmentTag()).commit();
    }
}
