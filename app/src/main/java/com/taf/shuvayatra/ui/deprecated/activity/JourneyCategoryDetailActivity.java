package com.taf.shuvayatra.ui.deprecated.activity;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;

import com.taf.shuvayatra.R;
import com.taf.shuvayatra.databinding.JourneyCategoryDetailDataBinding;
import com.taf.shuvayatra.util.AnalyticsUtil;
import com.taf.util.MyConstants;

import butterknife.BindView;

@Deprecated
public class JourneyCategoryDetailActivity extends CategoryDetailActivity {

    @BindView(R.id.app_bar)
    AppBarLayout mAppBar;

    @Override
    public String screenName() {
        return null;
    }

    @Override
    public int getLayout() {
        return R.layout.activity_journey_category_detail;
    }

    @Override
    public MyConstants.DataParent getDataParent() {
        return MyConstants.DataParent.JOURNEY;
    }

    @Override
    public void expandAppBar() {
        mAppBar.setExpanded(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((JourneyCategoryDetailDataBinding) mBinding).setCategory(mCategory);
        if (savedInstanceState == null) {
            AnalyticsUtil.logViewEvent(getAnalytics(), mCategory.getId(), mCategory.getTitle(),
                    "section_journey");
        }
    }
}
