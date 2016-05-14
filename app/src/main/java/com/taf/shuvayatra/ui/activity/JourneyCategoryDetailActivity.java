package com.taf.shuvayatra.ui.activity;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;

import com.taf.shuvayatra.R;
import com.taf.shuvayatra.base.CategoryDetailActivity;
import com.taf.shuvayatra.databinding.JourneyCategoryDetailDataBinding;
import com.taf.util.MyConstants;

import butterknife.Bind;

public class JourneyCategoryDetailActivity extends CategoryDetailActivity {

    @Bind(R.id.app_bar)
    AppBarLayout mAppBar;

    @Override
    public int getLayout() {
        return R.layout.activity_journey_category_detail;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((JourneyCategoryDetailDataBinding) mBinding).setCategory(mCategory);
    }

    @Override
    public MyConstants.DataParent getDataParent() {
        return MyConstants.DataParent.JOURNEY;
    }

    @Override
    public void expandAppBar(){
        mAppBar.setExpanded(true);
    }
}
