package com.taf.shuvayatra.base;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.test.mock.MockApplication;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.taf.data.utils.AppPreferences;
import com.taf.interactor.UseCaseData;
import com.taf.model.Country;
import com.taf.shuvayatra.MyApplication;
import com.taf.shuvayatra.R;
import com.taf.shuvayatra.di.component.ApplicationComponent;
import com.taf.shuvayatra.di.module.ActivityModule;
import com.taf.shuvayatra.util.Utils;
import com.taf.util.MyConstants;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity {

    public ViewDataBinding mBinding;
    @Inject
    protected AppPreferences mPreferences;
    @Nullable
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @Inject
    FirebaseAnalytics mAnalytics;


    public abstract int getLayout();

    public boolean isDataBindingEnabled() {
        return false;
    }

    public boolean containsShareOption() {
        return false;
    }

    public boolean containsFavouriteOption() {
        return false;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initLanguage();

        getApplicationComponent().inject(this);
        if (!isDataBindingEnabled()) {
            setContentView(getLayout());
            ButterKnife.bind(this);
        } else {
            mBinding = DataBindingUtil.setContentView(this, getLayout());
            ButterKnife.bind(this, mBinding.getRoot());
        }
        if (mToolbar != null)
            initializeToolbar();
    }

    protected void initLanguage() {
        Utils.setLanguage(MyConstants.Language.NEPALI, getApplicationContext());
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //ButterKnife.unbind(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        if (containsShareOption()) {
            getMenuInflater().inflate(R.menu.share, menu);
        }
        if (containsFavouriteOption()) {
            getMenuInflater().inflate(R.menu.favourite, menu);
        }
        return true;
    }

    public UseCaseData getUserCredentialsUseCase() {
        UseCaseData useCaseData = new UseCaseData();
        String gender = getPreferences().getGender();
        if (gender != null)
            if (gender.equalsIgnoreCase(getString(R.string.gender_male))) {
                useCaseData.putString(UseCaseData.USER_GENDER, "M");
            } else if (gender.equalsIgnoreCase(getString(R.string.gender_female))) {
                useCaseData.putString(UseCaseData.USER_GENDER, "F");
            } else if (gender.equalsIgnoreCase(getString(R.string.gender_other))) {
                useCaseData.putString(UseCaseData.USER_GENDER, "O");
            }
        String location = getPreferences().getLocation();
        useCaseData.putString(UseCaseData.COUNTRY_ID, location.equalsIgnoreCase(MyConstants
                .Preferences.DEFAULT_LOCATION) || location.equalsIgnoreCase(
                getString(R.string.country_not_decided_yet)) ? null : location.split(",")[Country.INDEX_ID]);
        return useCaseData;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.action_favourite:
                updateFavouriteState();
                break;
        }
        return true;
    }

    public void updateFavouriteState() {
    }

    private void initializeToolbar() {
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

    public FirebaseAnalytics getAnalytics() {
        return mAnalytics;
    }

    public ApplicationComponent getApplicationComponent() {
        return ((MyApplication) getApplication()).getApplicationComponent();
    }

    public ActivityModule getActivityModule() {
        return new ActivityModule(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.isActivityShowing = true;
        initLanguage();

    }

    @Override
    protected void onPause() {
        super.onPause();
       MyApplication.isActivityShowing = false;
    }
}
