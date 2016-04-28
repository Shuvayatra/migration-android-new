package com.taf.shuvayatra.ui.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.view.View;
import android.widget.FrameLayout;

import com.taf.shuvayatra.R;
import com.taf.shuvayatra.base.BaseActivity;
import com.taf.shuvayatra.ui.fragment.JourneyFragment;
import com.taf.shuvayatra.ui.fragment.DestinationFragment;
import com.taf.shuvayatra.ui.fragment.FeedFragment;
import com.taf.shuvayatra.ui.fragment.UserFragment;

import java.util.ArrayList;

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

        setUpToolbarLogo();
        setUpTabs();
        showFragment(4);
    }

    private void setUpToolbarLogo() {
        getToolbar().setLogo(R.mipmap.ic_launcher);
        getToolbar().setLogoDescription("logo");
        ArrayList<View> potentialViews = new ArrayList<>();
        getToolbar().findViewsWithText(potentialViews, "logo", View
                .FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
        View logoIcon = null;
        if (potentialViews.size() > 0) {
            logoIcon = potentialViews.get(0);
        }
        getToolbar().setLogoDescription(null);
        logoIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpTabs();
                showFragment(4);
            }
        });
    }

    private void setUpTabs() {
        String[] tabTitles = getResources().getStringArray(R.array.home_tabs);
        int[] tabIcons = {
                R.mipmap.ic_launcher,
                R.mipmap.ic_launcher,
                R.mipmap.ic_launcher,
                R.mipmap.ic_launcher
        };

        mTabLayout.removeAllTabs();
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

    private void showFragment(int position) {
        switch (position) {
            case 0:
                JourneyFragment journeyFragment = JourneyFragment.newInstance();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, journeyFragment).commit();
                break;
            case 1:
                DestinationFragment destinationFragment = DestinationFragment.newInstance();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, destinationFragment).commit();
                break;
            case 2:
                break;
            case 3:
                UserFragment userFragment = new UserFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, userFragment).commit();
                break;
            case 4:
                FeedFragment feedFragment = new FeedFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, feedFragment).commit();
                break;
        }
    }
}
