package com.taf.shuvayatra.ui.activity;

import android.os.Bundle;
import com.taf.shuvayatra.R;
import com.taf.shuvayatra.base.CategoryDetailActivity;
import com.taf.shuvayatra.databinding.JourneyCategoryDetailDataBinding;
import com.taf.util.MyConstants;

public class JourneyCategoryDetailActivity extends CategoryDetailActivity {


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
}
