package com.taf.shuvayatra.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

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
import com.taf.util.MyConstants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;

import butterknife.BindView;

public class OnBoardActivity extends BaseActivity implements OnBoardQuestionAdapter.ButtonPressListener,
        CountryView {

    public static final String TAG = "OnBoardActivity";
    @BindView(R.id.viewpager_questions)
    ViewPager mQuestionPager;

    @Inject
    CountryListPresenter presenter;

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
        mQuestionPager.setOffscreenPageLimit(6);

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
//        if (pos < OnBoardQuestionAdapter.LIST_SIZE - 1) {
//            mQuestionPager.setCurrentItem(pos + 1);
//        } else {
//            getPreferences().setFirstLaunch(false);
//            Intent intent = new Intent(this, HomeActivity.class);
//            startActivity(intent);
//            finish();
//        }
    }

    @Override
    public void onBackButtonPressed(int pos) {
        Logger.e(TAG, "pos: " + pos);
        mQuestionPager.setCurrentItem(--pos);
    }

    @Override
    public void renderCountries(List<Country> countryList) {

        // update preference
        // add into preference key value map of country as JSON
        List<String> countries = new ArrayList<>();
        for (Country country : countryList) {
            countries.add(String.valueOf(country.getId()) + "," + country.getTitle());
        }
        getPreferences().updateCountryListCallStatus(true);
        getPreferences().updateCountryList(new HashSet<>(countries));
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
        Logger.e(TAG, ">>> error: " + pErrorMessage);
        if (getString(R.string.exception_message_generic).equalsIgnoreCase(pErrorMessage)) {
//            // TODO: 10/26/16 remove this code
            Logger.e(TAG, ">>> generic exception caught");
        }

        if (getString(R.string.exception_message_no_connection).equalsIgnoreCase(pErrorMessage)) {
//            Snackbar.make()
        }
    }

    @Override
    public Context getContext() {
        return getApplicationContext();
    }
}
