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
import android.view.View;
import android.view.ViewGroup;

import com.taf.data.utils.Logger;
import com.taf.interactor.UseCaseData;
import com.taf.model.Country;
import com.taf.shuvayatra.R;
import com.taf.shuvayatra.base.BaseActivity;
import com.taf.shuvayatra.di.component.DaggerDataComponent;
import com.taf.shuvayatra.di.module.DataModule;
import com.taf.shuvayatra.presenter.CountryListPresenter;
import com.taf.shuvayatra.ui.adapter.OnBoardQuestionAdapter;
import com.taf.shuvayatra.ui.fragment.onboarding.AbroadQuestionFragment;
import com.taf.shuvayatra.ui.views.CountryView;
import com.taf.shuvayatra.ui.views.SwipeDisabledPager;
import com.taf.util.MyConstants;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class OnBoardActivity extends BaseActivity implements OnBoardQuestionAdapter.ButtonPressListener,
        CountryView {

    @BindView(R.id.viewpager_questions)
    SwipeDisabledPager mQuestionPager;

    // added for Snackbar
    @BindView(R.id.main_container)
    ViewGroup container;

    @Inject
    CountryListPresenter presenter;

    public static final String TAG = "OnBoardActivity";
    private static final int REQUEST_CODE_WIFI_SETTINGS = 1;

    private OnBoardQuestionAdapter pagerAdapter;

    @Override
    public int getLayout() {
        return R.layout.activity_on_board;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // add api request for country listing
        // api.shuvayatra.org/api/destinations
        pagerAdapter = new OnBoardQuestionAdapter(getSupportFragmentManager(), this);
        mQuestionPager.setAdapter(pagerAdapter);

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        if (!getPreferences().getFirstLaunch()) {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            finish();
        }

        initialize();

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
    public void onNextButtonPressed(int pos) {
        Logger.e(TAG, "pos: " + pos);
        // TODO: 10/26/16 refactor logic
        if (getPreferences().isOnBoardingCountryListLoaded()) {

            if (pos == OnBoardQuestionAdapter.LIST_SIZE - 1) {
                getPreferences().setFirstLaunch(false);
                Intent intent = new Intent(this, HomeActivity.class);
                startActivity(intent);
                finish();
            } else {
                mQuestionPager.setCurrentItem(pos + 1);
            }
        } else {

            if (pos == OnBoardQuestionAdapter.LIST_SIZE - 2) {
                getPreferences().setFirstLaunch(false);
                Intent intent = new Intent(this, HomeActivity.class);
                startActivity(intent);
                finish();
            } else {
                mQuestionPager.setCurrentItem(pos + 1);
            }
        }
    }

    @Override
    public void onBackButtonPressed(int pos) {
        Logger.e(TAG, "pos: " + pos);
        mQuestionPager.setCurrentItem(--pos);
    }

    @Override
    public void renderCountries(List<Country> countryList) {

        Logger.e(TAG, ">>> country size: " + countryList.size());
        // update preference
        // add into preference key value map of country as JSON
        List<String> countries = new ArrayList<>();
        for (Country country : countryList) {
            countries.add(String.valueOf(country.getId()) + "," + country.getTitle());
        }
        Logger.e(TAG, ">>> country list:\n" + countryList.toString());
        Logger.e(TAG, ">>> countries:\n" + countries.toString());
        getPreferences().updateCountryListCallStatus(true);
        getPreferences().updateCountryList(new HashSet<>(countries));

        // update fragment if created
        if (pagerAdapter.getFragment(MyConstants.OnBoarding.WORK_STATUS) != null) {
            ((AbroadQuestionFragment) pagerAdapter.getFragment(MyConstants.OnBoarding.WORK_STATUS))
                    .mButtonNext.setText(getString(R.string.next));
        }
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
