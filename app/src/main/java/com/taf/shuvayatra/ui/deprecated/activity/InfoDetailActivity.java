package com.taf.shuvayatra.ui.deprecated.activity;

import android.os.Bundle;
import com.taf.shuvayatra.R;
import com.taf.util.MyConstants;
@Deprecated
public class InfoDetailActivity extends CategoryDetailActivity {

    @Override
    public String screenName() {
        return null;
    }

    @Override
    public int getLayout() {
        return R.layout.activity_info_detail;
    }

    @Override
    public MyConstants.DataParent getDataParent() {
        return MyConstants.DataParent.INFO;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle(mCategory.getTitle());
    }

    @Override
    public boolean isDataBindingEnabled() {
        return false;
    }
}
