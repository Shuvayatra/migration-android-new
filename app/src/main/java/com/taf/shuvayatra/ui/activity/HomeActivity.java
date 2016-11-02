package com.taf.shuvayatra.ui.activity;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;

import com.taf.shuvayatra.R;
import com.taf.shuvayatra.base.BaseActivity;
import com.taf.shuvayatra.ui.fragment.DestinationFragment;
import com.taf.shuvayatra.ui.fragment.HomeFragment;
import com.taf.shuvayatra.ui.fragment.JourneyFragment;

import butterknife.BindView;

public class HomeActivity extends BaseActivity implements
        NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawer;
    @BindView(R.id.nav_view)
    NavigationView mNavigationView;

    @Override
    public int getLayout() {
        return R.layout.activity_home;
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
        mNavigationView.setCheckedItem(R.id.nav_home);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_home, HomeFragment.getInstance(), HomeFragment.TAG)
                .commit();
    }

    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
            return;
        }
        if(!(getSupportFragmentManager().getFragments().get(0) instanceof HomeFragment)) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_home, HomeFragment.getInstance(), HomeFragment.TAG)
                    .commit();
            mNavigationView.setCheckedItem(R.id.nav_home);
            return;
        }
        super.onBackPressed();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Fragment fragment = null;
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_home:
                fragment = getSupportFragmentManager().findFragmentByTag(HomeFragment.TAG);
                if(fragment == null) {
                    fragment = HomeFragment.getInstance();
                }
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_home, fragment, HomeFragment.TAG)
                        .commit();
                break;
            case R.id.nav_journey:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_home, JourneyFragment.getInstance(), JourneyFragment.TAG)
                        .commit();
                break;
            case R.id.nav_radio:
                break;
            case R.id.nav_destination:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_home, DestinationFragment.newInstance(), DestinationFragment.TAG)
                        .commit();
                break;
        }
        mDrawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
