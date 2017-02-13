package com.taf.shuvayatra.ui.deprecated.activity;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;

import com.taf.shuvayatra.R;
import com.taf.shuvayatra.databinding.CountryDetailDataBinding;
import com.taf.shuvayatra.util.AnalyticsUtil;
import com.taf.util.MyConstants;

import butterknife.BindView;
@Deprecated
public class CountryDetailActivity extends CategoryDetailActivity {

    @BindView(R.id.app_bar)
    AppBarLayout mAppBar;

    @Override
    public String screenName() {
        return null;
    }

    @Override
    public int getLayout() {
        return R.layout.activity_country_detail;
    }

    @Override
    public MyConstants.DataParent getDataParent() {
        return MyConstants.DataParent.COUNTRY;
    }

    @Override
    public void expandAppBar() {
        mAppBar.setExpanded(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((CountryDetailDataBinding) mBinding).setCountry(mCategory);
        if (savedInstanceState == null) {
            AnalyticsUtil.logViewEvent(getAnalytics(), mCategory.getId(), mCategory.getTitle(),
                    "country");
        }
    }
}
