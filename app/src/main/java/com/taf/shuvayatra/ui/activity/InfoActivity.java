package com.taf.shuvayatra.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.MenuItem;

import com.taf.shuvayatra.R;
import com.taf.shuvayatra.base.BaseActivity;
import com.taf.shuvayatra.base.MediaServiceActivity;
import com.taf.shuvayatra.ui.fragment.InfoFragment;

import static com.taf.util.MyConstants.Extras.KEY_ABOUT;
import static com.taf.util.MyConstants.Extras.KEY_CONTACT_US;
import static com.taf.util.MyConstants.Extras.KEY_INFO;

public class InfoActivity extends BaseActivity {

    private static final String TAG = "InfoActivity";

    @Override
    public int getLayout() {
        return R.layout.activity_info_detail;
    }

    @Override
    public String screenName() {
        return "About/Contact Screen";
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle extra = getIntent().getExtras();
        if (savedInstanceState != null) {
            extra.putAll(savedInstanceState);
        }

        setFragment(extra.getString(KEY_INFO));
    }

    private void setFragment(String key) {
        Fragment fragment = null;
        switch (key) {
            case KEY_ABOUT:
                fragment = getSupportFragmentManager().findFragmentByTag("AboutFragment");
                if (fragment == null) {
                    fragment = InfoFragment.newInstance(KEY_ABOUT);
                }
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, fragment, "AboutFragment")
                        .commit();
                break;
            case KEY_CONTACT_US:
                fragment = getSupportFragmentManager().findFragmentByTag("ContactFragment");
                if (fragment == null) {
                    fragment = InfoFragment.newInstance(KEY_CONTACT_US);
                }
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, fragment, "ContactFragment")
                        .commit();
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
