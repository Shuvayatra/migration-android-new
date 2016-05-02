package com.taf.shuvayatra.ui.activity;

import android.os.Bundle;
import com.taf.shuvayatra.R;
import com.taf.shuvayatra.base.CategoryDetailActivity;
import com.taf.util.MyConstants;

public class InfoDetailActivity extends CategoryDetailActivity {

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
