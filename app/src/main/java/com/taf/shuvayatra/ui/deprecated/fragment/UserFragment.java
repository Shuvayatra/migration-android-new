package com.taf.shuvayatra.ui.deprecated.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.widget.LinearLayout;

import com.taf.data.utils.Logger;
import com.taf.shuvayatra.R;
import com.taf.shuvayatra.base.BaseFragment;
import com.taf.shuvayatra.ui.deprecated.adapter.UserSectionPagerAdapter;

import butterknife.BindView;
@Deprecated
public class UserFragment extends BaseFragment {

    @BindView(R.id.viewpager)
    ViewPager mViewPager;
    @BindView(R.id.tabs)
    TabLayout mTabs;
    @BindView(R.id.data_container)
    LinearLayout mContainer;

    UserSectionPagerAdapter mPagerAdapter;
    int currentTab = 0;
    public static final String KEY_CURRENT_TAB = "current_tab";

    public UserFragment() {
        // Required empty public constructor
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_user;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState!= null){
            currentTab = savedInstanceState.getInt(KEY_CURRENT_TAB);
        }
        mPagerAdapter = new UserSectionPagerAdapter(getChildFragmentManager(), getActivity());
        mViewPager.setAdapter(mPagerAdapter);
        Logger.e("UserFragment", "acticity created");
        mViewPager.setCurrentItem(currentTab, false);
        mTabs.setupWithViewPager(mViewPager);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
//        currentTab = mTabs.getSelectedTabPosition();
        outState.putSerializable(KEY_CURRENT_TAB,currentTab);
        super.onSaveInstanceState(outState);
    }
}
