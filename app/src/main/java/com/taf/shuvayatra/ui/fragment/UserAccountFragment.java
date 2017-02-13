package com.taf.shuvayatra.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.taf.shuvayatra.R;
import com.taf.shuvayatra.base.BaseNavigationFragment;
import com.taf.shuvayatra.ui.adapter.UserAccountPagerAdapter;

import butterknife.BindView;

/**
 * Created by umesh on 1/11/17.
 */

public class UserAccountFragment extends BaseNavigationFragment {

    public static final String TAG = "UserAccountFragment";
    private static final int REQUEST_CODE_POST_DETAIL = 1001;

    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    @BindView(R.id.tabs)
    TabLayout mTabs;

    public static UserAccountFragment newInstance() {
        return new UserAccountFragment();
    }

    @Override
    public String screenName() {
        return "Navigation - User Account";
    }

    @Override
    public Fragment defaultInstance() {
        return newInstance();
    }

    @Override
    public String fragmentTag() {
        return TAG;
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_user_account;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setUpAdapter();
    }


    private void setUpAdapter() {
        UserAccountPagerAdapter adapter = new UserAccountPagerAdapter(getContext(), getChildFragmentManager());
        mViewPager.setAdapter(adapter);
        mTabs.setupWithViewPager(mViewPager);
    }
}
