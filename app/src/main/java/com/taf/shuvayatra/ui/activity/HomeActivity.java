package com.taf.shuvayatra.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.taf.shuvayatra.R;
import com.taf.shuvayatra.base.MediaServiceActivity;
import com.taf.shuvayatra.ui.fragment.ChannelFragment;
import com.taf.shuvayatra.ui.fragment.DestinationFragment;
import com.taf.shuvayatra.ui.fragment.HomeFragment;
import com.taf.shuvayatra.ui.fragment.JourneyFragment;
import com.taf.shuvayatra.ui.fragment.NewsFragment;
import com.taf.shuvayatra.ui.fragment.UserAccountFragment;
import com.taf.util.MyConstants;

import butterknife.BindView;

public class HomeActivity extends MediaServiceActivity implements
        NavigationView.OnNavigationItemSelectedListener {

    public static final String TAG = "HomeActivity";
    private static final String STATE_MENU_ID = "nav-selected-menu-id";

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawer;
    @BindView(R.id.nav_view)
    NavigationView mNavigationView;
    @BindView(R.id.searchbox_container)
    LinearLayout mSearchBox;

    int selectedNavMenuId;

    private BroadcastReceiver mRadioCallbackReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(ChannelFragment.TAG);
            if (fragment == null) {
                fragment = ChannelFragment.getInstance();
            }
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_home, fragment, ChannelFragment.TAG)
                    .commit();
            mNavigationView.setCheckedItem(R.id.nav_radio);
        }
    };

    @Override
    public int getLayout() {
        return R.layout.activity_home;
    }

    protected void onPause() {
        super.onPause();
        unregisterReceiver(mRadioCallbackReceiver);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, getToolbar(), R.string.navigation_drawer_open, R.string
                .navigation_drawer_close);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();

        mNavigationView.setNavigationItemSelectedListener(this);
        if(savedInstanceState !=null ){
            selectedNavMenuId = savedInstanceState.getInt(STATE_MENU_ID);
        }else{
            selectedNavMenuId = R.id.nav_home;
        }
        mNavigationView.setCheckedItem(selectedNavMenuId);
        showFragment(selectedNavMenuId);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mSearchBox
                .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), SearchActivity.class);
                startActivity(intent);
                overridePendingTransition(0,0);
            }
        });
    }

    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(MyConstants.Intent.ACTION_SHOW_RADIO);
        registerReceiver(mRadioCallbackReceiver, filter);
    }

    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
            return;
        }
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(HomeFragment.TAG);
        if(fragment==null){
            mNavigationView.setCheckedItem(R.id.nav_home);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_home, HomeFragment.getInstance(), HomeFragment.TAG)
                    .commit();
            return;
        }
        super.onBackPressed();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        showFragment(id);
        mDrawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /*
    Replace the fragment by their menu Id from navigation drawer
     */

    private void showFragment(int menuId){
        Fragment fragment = null;

        switch (menuId) {
            case R.id.nav_home:
                fragment = getSupportFragmentManager().findFragmentByTag(HomeFragment.TAG);
                if (fragment == null) {
                    fragment = HomeFragment.getInstance();
                }
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_home, fragment, HomeFragment.TAG)
                        .commit();
                break;
            case R.id.nav_journey:
                fragment = getSupportFragmentManager().findFragmentByTag(JourneyFragment.TAG);
                if (fragment == null) {
                    fragment = JourneyFragment.getInstance();
                }
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_home, fragment, JourneyFragment.TAG)
                        .commit();
                break;
            case R.id.nav_radio:
                fragment = getSupportFragmentManager().findFragmentByTag(ChannelFragment.TAG);
                if (fragment == null) {
                    fragment = ChannelFragment.getInstance();
                }
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_home, fragment, ChannelFragment.TAG)
                        .commit();
                break;
            case R.id.nav_destination:
                fragment = getSupportFragmentManager().findFragmentByTag(DestinationFragment.TAG);
                if (fragment == null) {
                    fragment = DestinationFragment.newInstance();
                }
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_home, fragment, DestinationFragment.TAG)
                        .commit();
                break;
            case R.id.nav_news:
                fragment = getSupportFragmentManager().findFragmentByTag(NewsFragment.TAG);
                if (fragment == null) {
                    fragment = NewsFragment.newInstance();
                }
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_home, fragment, NewsFragment.TAG)
                        .commit();
                break;
            case R.id.nav_account:
                fragment = getSupportFragmentManager().findFragmentByTag(UserAccountFragment.TAG);
                if(fragment == null){
                    fragment = UserAccountFragment.newInstance();
                }
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_home, fragment, UserAccountFragment.TAG)
                        .commit();
                break;
        }
        if(fragment!=null) selectedNavMenuId = menuId;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(STATE_MENU_ID, selectedNavMenuId);
        super.onSaveInstanceState(outState);
    }
}
