package com.taf.shuvayatra.ui.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.widget.FrameLayout;

import com.taf.shuvayatra.R;
import com.taf.shuvayatra.base.BaseActivity;
import com.taf.shuvayatra.ui.fragment.HomeFeedFragment;

import butterknife.Bind;

public class MainActivity extends BaseActivity {

    @Bind(R.id.tabs)
    TabLayout mTabLayout;
    @Bind(R.id.fragment_container)
    FrameLayout mFragmentContainer;

    @Override
    public int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        setUpTabs();
        showFragment(4);
    }

    private void setUpTabs(){
        String[] tabTitles = getResources().getStringArray(R.array.home_tabs);
        int[] tabIcons = {
                R.mipmap.ic_launcher,
                R.mipmap.ic_launcher,
                R.mipmap.ic_launcher,
                R.mipmap.ic_launcher
        };

        for (int i = 0; i < tabTitles.length; i++) {
            TabLayout.Tab tab = mTabLayout.newTab()
                    .setIcon(tabIcons[i])
                    .setText(tabTitles[i]);
            mTabLayout.addTab(tab, false);
        }

        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                showFragment(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    private void showFragment(int position){
        switch (position){
            case 0:
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                HomeFeedFragment homeFeedFragment = new HomeFeedFragment();
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction().add(R.id.fragment_container,homeFeedFragment).commit();
                break;
        }
    }
}