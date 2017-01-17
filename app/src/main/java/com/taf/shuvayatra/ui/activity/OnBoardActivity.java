package com.taf.shuvayatra.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.taf.data.utils.Logger;
import com.taf.interactor.UseCaseData;
import com.taf.model.Country;
import com.taf.model.UserInfoModel;
import com.taf.shuvayatra.R;
import com.taf.shuvayatra.base.BaseActivity;
import com.taf.shuvayatra.di.component.DaggerDataComponent;
import com.taf.shuvayatra.di.module.DataModule;
import com.taf.shuvayatra.presenter.CountryListPresenter;
import com.taf.shuvayatra.presenter.OnBoardingPresenter;
import com.taf.shuvayatra.ui.adapter.OnBoardQuestionAdapter;
import com.taf.shuvayatra.ui.fragment.UserInfoFragment;
import com.taf.shuvayatra.ui.fragment.onboarding.AbroadQuestionFragment;
import com.taf.shuvayatra.ui.views.CountryView;
import com.taf.shuvayatra.ui.views.OnBoardingView;
import com.taf.shuvayatra.ui.views.SwipeDisabledPager;
import com.taf.util.MyConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;

public class OnBoardActivity extends BaseActivity implements
        OnBoardQuestionAdapter.ButtonPressListener,
        CountryView {

    @BindView(R.id.viewpager_questions)
    SwipeDisabledPager mQuestionPager;

    // added for Snackbar
    @BindView(R.id.main_container)
    ViewGroup container;

    @Inject
    CountryListPresenter presenter;

    /**
     * indicator for {@link #finish()}
     */
    boolean isExit = false;
    /**
     * if {@link #isExit} is true, maintain past userInfoModel and check against new userInfoModel
     */
    UserInfoModel userInfoModel;

    public static final String TAG = "OnBoardActivity";
    private static final int REQUEST_CODE_WIFI_SETTINGS = 1;

    private int currentItem;

    @Override
    public int getLayout() {
        return R.layout.activity_on_board;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Logger.e(TAG, ">>> ON CREATE <<<");

        // add api request for country listing
        // api.shuvayatra.org/api/destinations
        initialize();

        isExit = getIntent().getBooleanExtra(MyConstants.Extras.IS_EXIT, false);
        userInfoModel = (UserInfoModel) getIntent().getSerializableExtra(UserInfoModel.EXTRA_INFO_MODEL);

        OnBoardQuestionAdapter pagerAdapter = new OnBoardQuestionAdapter(getSupportFragmentManager(), this);
        mQuestionPager.setAdapter(pagerAdapter);
        // add off screen page limit to avoid context NPE on fragment
        //todo fix for setOffscreenPageLimit is taking min value 2 by default.
        mQuestionPager.setOffscreenPageLimit(2);
        mQuestionPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                currentItem = position;
            }
        });

        if (savedInstanceState != null) {
            currentItem = savedInstanceState.getInt(MyConstants.Extras.KEY_PAGER_POSITION, 0);
            mQuestionPager.setCurrentItem(currentItem);
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    private void onOnboardingDone() {
        if (isExit) {
            setResult(RESULT_OK);
            if (userInfoModel != null && !userInfoModel.equals(UserInfoFragment
                    .makeUserInfo(getPreferences(), getContext()))) {
                getPreferences().setUserInfoSyncStatus(false);
            }
            finish();
        } else {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_onboarding, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_skip) {
            getPreferences().setFirstLaunch(false);
            getPreferences().setUserOnBoardingComplete(false);
            onOnboardingDone();
        }
        return super.onOptionsItemSelected(item);
    }

    public void initialize() {
        DaggerDataComponent.builder().dataModule(new DataModule())
                .activityModule(this.getActivityModule())
                .applicationComponent(this.getApplicationComponent())
                .build()
                .inject(this);
        presenter.attachView(this);
        presenter.initialize(new UseCaseData());
    }

    @Override
    public void onNextButtonPressed(final int position) {
        // TODO: 10/26/16 refactor logic
        if (getPreferences().isOnBoardingCountryListLoaded()) {
            if (position == OnBoardQuestionAdapter.LIST_SIZE - 1) {
                getPreferences().setFirstLaunch(false);
                getPreferences().setUserOnBoardingComplete(true);
                onOnboardingDone();
            } else {
                mQuestionPager.setCurrentItem(position + 1);
            }
        }
    }


    @Override
    public void onBackButtonPressed(int position) {
        mQuestionPager.setCurrentItem(--position);
    }

    @Override
    public void renderCountries(List<Country> countryList) {

        // update preference
        // add into preference key value map of country as JSON
        if (!countryList.isEmpty()) {
            List<String> countries = new ArrayList<>();
            for (Country country : countryList) {
                countries.add(String.valueOf(country.getId()) + "," + country.getTitle());
            }
            getPreferences().updateCountryListCallStatus(true);

            // update fragment if created
            if (((OnBoardQuestionAdapter) mQuestionPager.getAdapter()).getFragment(MyConstants.OnBoarding.WORK_STATUS) != null) {
                ((AbroadQuestionFragment) ((OnBoardQuestionAdapter) mQuestionPager.getAdapter())
                        .getFragment(MyConstants.OnBoarding.WORK_STATUS))
                        .mButtonNext.setText(getString(R.string.next));
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // bug fix for unresponsive page change
        if (mQuestionPager != null) {
            Logger.e(TAG, ">>> on save instance state called <<<");
            outState.putInt(MyConstants.Extras.KEY_PAGER_POSITION, currentItem);
            Logger.e(TAG, ">>> set adapter null<<<");
            mQuestionPager.setAdapter(null);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Logger.e(TAG, ">>> RESUME <<<");

        // because we used saved instance to set adapter as null, there are instances where onCreate()
        // is never called. For those instances, this change was made
        if (mQuestionPager.getAdapter() == null) {
            OnBoardQuestionAdapter pagerAdapter = new OnBoardQuestionAdapter(getSupportFragmentManager(), this);
            mQuestionPager.setAdapter(pagerAdapter);
            mQuestionPager.setCurrentItem(currentItem);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.destroy();
    }

    @Override
    public void showLoadingView() {
        // do nothing
    }

    @Override
    public void hideLoadingView() {
        // do nothing
    }

    @Override
    public void showErrorView(String pErrorMessage) {
        // ask to check network via snackbar
        if (getString(R.string.exception_message_no_connection).equalsIgnoreCase(pErrorMessage)) {
            Snackbar.make(container, getString(R.string.exception_message_no_connection),
                    Snackbar.LENGTH_LONG).setAction(getString(R.string.action_on), new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // register for listener
                    // send to wifi settings
                    startActivityForResult(new Intent(Settings.ACTION_WIFI_SETTINGS), REQUEST_CODE_WIFI_SETTINGS);
                }
            }).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_WIFI_SETTINGS) {
            // TODO: 10/27/16 replace with RxJava's delay operator
            // check internet connection on delay?
            new Handler(Looper.getMainLooper()).postDelayed(
                    new Runnable() {
                        @Override
                        public void run() {
                            ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
                            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
                            boolean isConnected = (networkInfo != null && networkInfo.isConnectedOrConnecting());
                            if (isConnected) {
                                // initialize use case data again
                                presenter.initialize(new UseCaseData());
                            }
                        }
                    }, 3000);
        }
    }

    @Override
    public Context getContext() {
        return getApplicationContext();
    }

}
