package com.taf.shuvayatra.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;

import com.yipl.nrna.R;
import com.yipl.nrna.base.BaseFragment;
import com.yipl.nrna.ui.adapter.InfoCenterPagerAdapter;

import butterknife.Bind;

public class InfoCenterFragment extends BaseFragment {

    @Bind(R.id.viewpager)
    ViewPager mViewPager;
    @Bind(R.id.tabs)
    TabLayout mTabs;
    @Bind(R.id.data_container)
    LinearLayout mContainer;

    InfoCenterPagerAdapter mPagerAdapter;

    public InfoCenterFragment() {
        // Required empty public constructor
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_info_center;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPagerAdapter = new InfoCenterPagerAdapter(getChildFragmentManager(), getContext());

        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setOffscreenPageLimit(3);
        mTabs.setupWithViewPager(mViewPager);
    }

    @Override
    public void showNewContentInfo() {
        Snackbar.make(mContainer, getString(R.string.message_content_available), Snackbar
                .LENGTH_INDEFINITE)
                .setAction(getString(R.string.action_refresh), new View.OnClickListener() {
                    @Override
                    public void onClick(View pView) {
                        mPagerAdapter.notifyDataSetChanged();
                    }
                })
                .show();
    }
}
