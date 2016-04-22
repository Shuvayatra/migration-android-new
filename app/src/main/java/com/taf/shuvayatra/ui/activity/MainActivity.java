package com.taf.shuvayatra.ui.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.taf.shuvayatra.R;
import com.taf.shuvayatra.base.BaseActivity;
import com.taf.shuvayatra.ui.fragment.FeedFragment;
import com.taf.shuvayatra.ui.fragment.JourneyFragment;

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==android.R.id.home){
            showFragment(4);
            return true;
        }
        return super.onOptionsItemSelected(item);
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
                JourneyFragment journeyFragment = JourneyFragment.newInstance();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, journeyFragment).commit();
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                FeedFragment feedFragment = new FeedFragment();
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction().add(R.id.fragment_container, feedFragment).commit();
                break;
        }
    }
}
