package com.taf.shuvayatra.ui.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.MenuItem;

import com.taf.data.utils.Logger;
import com.taf.shuvayatra.R;
import com.taf.shuvayatra.base.BaseActivity;
import com.taf.shuvayatra.ui.fragment.CountryWidgetFragment;
import com.taf.shuvayatra.ui.fragment.HomeFragment;


public class DeepLinkActivity extends BaseActivity {

    @Override
    public int getLayout() {
        return R.layout.activity_deep_link;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Uri data = getIntent().getData();
        handleDeepLink(data);

        getSupportActionBar().setTitle(getIntent().getStringExtra("title"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void handleDeepLink(Uri data) {
        Fragment fragment = null;
        Logger.d("DeepLinkActivity_handleDeepLink", "data: " + data.toString());
        Logger.d("DeepLinkActivity_handleDeepLink", "params: " + data.getQueryParameterNames());

        if (data.getHost().equals("journey")) {

        } else if (data.getHost().equals("destination")) {
            fragment = new CountryWidgetFragment();
        } else if (data.getHost().equals("news")) {
            fragment = new HomeFragment();
        } else if (data.getHost().equals("radio")) {

        } else if (data.getHost().equals("qna")) {

        }

        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment, "deep-link")
                    .commit();
        }
    }
}
