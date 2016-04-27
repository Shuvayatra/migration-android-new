package com.taf.shuvayatra.ui.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.taf.data.utils.Logger;
import com.taf.model.Category;
import com.taf.shuvayatra.R;
import com.taf.shuvayatra.base.BaseActivity;
import com.taf.shuvayatra.base.CategoryDetailActivity;
import com.taf.shuvayatra.databinding.CountryDetailDataBinding;
import com.taf.shuvayatra.ui.fragment.FeedFragment;
import com.taf.util.MyConstants;

import java.util.List;

public class CountryDetailActivity extends CategoryDetailActivity {

    @Override
    public int getLayout() {
        return R.layout.activity_country_detail;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((CountryDetailDataBinding) mBinding).setCountry(mCategory);
    }

    @Override
    public MyConstants.DataParent getDataParent() {
        return MyConstants.DataParent.COUNTRY;
    }
}
