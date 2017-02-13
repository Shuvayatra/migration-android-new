package com.taf.shuvayatra.base;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.taf.model.ScreenModel;
import com.taf.shuvayatra.base.BaseFragment;
import com.taf.shuvayatra.ui.interfaces.IDynamicNavigationFragment;

/**
 *
 */
public abstract class BaseDynamicNavigationFragment extends BaseFragment implements IDynamicNavigationFragment {

    public abstract Fragment defaultInstance(ScreenModel screenModel);

    public abstract String fragmentTag();

    @Override
    public void initDynamicNavigation(ScreenModel screenModel, int layout,
                                      FragmentManager fragmentManager) {
        Fragment fragment = fragmentManager.findFragmentByTag(fragmentTag()) == null ?
                defaultInstance(screenModel) : fragmentManager.findFragmentByTag(fragmentTag());
        fragmentManager.beginTransaction().replace(layout, fragment, fragmentTag()).commit();
    }

    @Override
    public void initNavigation(int layout, FragmentManager fragmentManager) {
        // space for rent
    }
}
