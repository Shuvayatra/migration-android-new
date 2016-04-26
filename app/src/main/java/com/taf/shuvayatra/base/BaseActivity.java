package com.taf.shuvayatra.base;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.taf.shuvayatra.MyApplication;
import com.taf.shuvayatra.R;
import com.taf.shuvayatra.di.component.ApplicationComponent;
import com.taf.shuvayatra.di.module.ActivityModule;
import com.taf.shuvayatra.util.AppPreferences;
import com.taf.shuvayatra.util.Utils;
import com.taf.util.MyConstants;

import butterknife.Bind;
import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity {

    public ViewDataBinding mBinding;
    @Nullable
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    AppPreferences mPreferences;

    public abstract int getLayout();

    public boolean isDataBindingEnabled() {
        return false;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        initLanguage();
        super.onCreate(savedInstanceState);
        getApplicationComponent().inject(this);
        if (!isDataBindingEnabled()) {
            setContentView(getLayout());
            ButterKnife.bind(this);
        } else {
            mBinding = DataBindingUtil.setContentView(this, getLayout());
            ButterKnife.bind(this, mBinding.getRoot());
        }
        initializeToolbar();

        mPreferences = new AppPreferences(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initLanguage();
    }

    private void initLanguage() {
        Utils.setLanguage(MyConstants.Language.NEPALI, getApplicationContext());
    }

    private void initializeToolbar() {
        //mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar == null) {
            throw new IllegalStateException("Layout is required to include a Toolbar with id " +
                    "'toolbar'");
        }
        setUpToolbarMenu();
        setSupportActionBar(mToolbar);
    }

    public void setUpToolbarMenu() {
        //mToolbar.inflateMenu(R.menu.main);
    }

    protected Toolbar getToolbar() {
        return mToolbar;
    }

    public AppPreferences getPreferences() {
        return mPreferences;
    }

    public ApplicationComponent getApplicationComponent() {
        return ((MyApplication) getApplication()).getApplicationComponent();
    }

    public ActivityModule getActivityModule() {
        return new ActivityModule(this);
    }
}
