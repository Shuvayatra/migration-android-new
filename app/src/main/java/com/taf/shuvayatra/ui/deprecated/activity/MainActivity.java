package com.taf.shuvayatra.ui.deprecated.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.view.View;
import android.widget.FrameLayout;

import com.taf.data.utils.Logger;
import com.taf.shuvayatra.R;
import com.taf.shuvayatra.base.BaseActivity;
import com.taf.shuvayatra.ui.deprecated.fragment.DestinationFragment;
import com.taf.shuvayatra.ui.deprecated.fragment.FeedFragment;
import com.taf.shuvayatra.ui.deprecated.fragment.InfoFragment;
import com.taf.shuvayatra.ui.deprecated.fragment.JourneyFragment;
import com.taf.shuvayatra.ui.deprecated.fragment.UserFragment;
import com.taf.shuvayatra.ui.fragment.ChannelFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MainActivity extends BaseActivity {

    private static final String CURRENT_FRAGMENT_POS = "currentFragmentPosition";
    @BindView(R.id.tabs)
    TabLayout mTabLayout;
    @BindView(R.id.fragment_container)
    FrameLayout mFragmentContainer;

    int currentFragment = 4;

    @Override
    public int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("");
        Logger.e("MainActivity", "onActivity created");
        if (savedInstanceState != null) {
            currentFragment = savedInstanceState.getInt(CURRENT_FRAGMENT_POS, 4);
        }
        setUpToolbarLogo();
        setUpTabs();
        showFragment(currentFragment);
    }

    private void setUpToolbarLogo() {
        getToolbar().setLogo(R.drawable.ic_logo);
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
                showFragment(4);
                setUpTabs();
            }
        });
    }

    private void setUpTabs() {
        String[] tabTitles = getResources().getStringArray(R.array.home_tabs);
        final int[] tabIcons = {
                R.drawable.ic_journey,
                R.drawable.ic_destination,
                R.drawable.ic_info,
                R.drawable.ic_user
        };
        final int[] tabIconsFaded = {
                R.drawable.ic_journey_faded,
                R.drawable.ic_destination_faded,
                R.drawable.ic_info_faded,
                R.drawable.ic_user_faded
        };

        mTabLayout.removeAllTabs();
        for (int i = 0; i < tabTitles.length; i++) {
            TabLayout.Tab tab = mTabLayout.newTab()
                    .setIcon(i == currentFragment ? tabIcons[i] : tabIconsFaded[i])
                    .setText(tabTitles[i]);
            mTabLayout.addTab(tab, i == currentFragment);
        }

        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab.setIcon(tabIcons[tab.getPosition()]);
                showFragment(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.setIcon(tabIconsFaded[tab.getPosition()]);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                tab.setIcon(tabIcons[tab.getPosition()]);
                showFragment(tab.getPosition());
            }
        });
    }

    private void showFragment(int position) {
        currentFragment = position;
        switch (position) {
            case 0:
                JourneyFragment journeyFragment = (JourneyFragment) getSupportFragmentManager()
                        .findFragmentByTag("journey");
                if (journeyFragment == null) {
                    journeyFragment = JourneyFragment.newInstance();
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        journeyFragment, "journey").commit();
                break;
            case 1:
                DestinationFragment destinationFragment = (DestinationFragment)
                        getSupportFragmentManager().findFragmentByTag("destination");
                if (destinationFragment == null) {
                    destinationFragment = DestinationFragment.newInstance();
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        destinationFragment, "destination").commit();
                break;
            case 2:
                InfoFragment infoFragment = (InfoFragment) getSupportFragmentManager()
                        .findFragmentByTag("info");
                if (infoFragment == null) {
                    infoFragment = InfoFragment.getInstance();
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        infoFragment, "info").commit();
                break;
            case 3:
                UserFragment userFragment = (UserFragment) getSupportFragmentManager()
                        .findFragmentByTag("user");
                if (userFragment == null) {
                    userFragment = new UserFragment();
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        userFragment, "user").commit();
                break;
            case 4:
                List<String> excludes = new ArrayList<>();
                excludes.add("place");
                FeedFragment feedFragment = (FeedFragment) getSupportFragmentManager()
                        .findFragmentByTag("home");
                if (feedFragment == null) {
                    feedFragment = FeedFragment.newInstance(excludes);
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        feedFragment, "home").commit();
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(CURRENT_FRAGMENT_POS, currentFragment);
        super.onSaveInstanceState(outState);
    }
}
