package com.taf.shuvayatra.ui.fragment.onboarding;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.google.firebase.messaging.FirebaseMessaging;
import com.taf.data.utils.Logger;
import com.taf.interactor.UseCaseData;
import com.taf.model.Country;
import com.taf.shuvayatra.R;
import com.taf.shuvayatra.base.BaseActivity;
import com.taf.shuvayatra.base.BaseFragment;
import com.taf.shuvayatra.di.component.DaggerDataComponent;
import com.taf.shuvayatra.di.module.DataModule;
import com.taf.shuvayatra.presenter.CountryListPresenter;
import com.taf.shuvayatra.ui.activity.OnBoardActivity;
import com.taf.shuvayatra.ui.adapter.CountryDropDownAdapter;
import com.taf.shuvayatra.ui.adapter.DropDownAdapter;
import com.taf.shuvayatra.ui.adapter.OnBoardQuestionAdapter;
import com.taf.shuvayatra.ui.views.CountryView;
import com.taf.util.MyConstants;
import com.taf.util.MyConstants.Preferences;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * This fragment is seen in {@link OnBoardActivity} as a part of the on-boarding question and is
 * only shown if {@link OnBoardActivity#renderCountries(List)} is called.
 * <p>
 *
 * @see OnBoardActivity
 */
public class CountryFragment extends BaseFragment implements CountryView, AdapterView.OnItemSelectedListener {

    @BindView(R.id.spinner_country)
    Spinner spinnerCountry;
    @Inject
    CountryListPresenter presenter;

    private static final String TAG = "CountryFragment";

    private OnBoardQuestionAdapter.ButtonPressListener mButtonListener;

    List<String> dataList;

    public BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            List<Country> countries = (List<Country>) intent
                    .getSerializableExtra(MyConstants.OnBoarding.INTENT_COUNTRY);
            renderCountries(countries);
            LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(this);
        }
    };

    public static CountryFragment newInstance(OnBoardQuestionAdapter.ButtonPressListener listener) {
        CountryFragment fragment = new CountryFragment();
        fragment.mButtonListener = listener;
        fragment.setRetainInstance(true);
        return fragment;
    }


    @Override
    public int getLayout() {
        return R.layout.fragment_country;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LocalBroadcastManager.getInstance(getContext())
                .registerReceiver(mBroadcastReceiver,
                        new IntentFilter(MyConstants.OnBoarding.INTENT_ACTION_REFRESH_COUNTRIES));

        initialize();
    }

    public void initialize() {
        DaggerDataComponent.builder().dataModule(new DataModule())
                .activityModule(((BaseActivity) getActivity()).getActivityModule())
                .applicationComponent(((BaseActivity) getActivity()).getApplicationComponent())
                .build().inject(this);
        presenter.attachView(this);
    }

    @OnClick(R.id.button_back)
    void onBack() {
        if (mButtonListener == null)
            mButtonListener = ((OnBoardQuestionAdapter.ButtonPressListener) getActivity());
        mButtonListener.onBackButtonPressed(MyConstants.OnBoarding.PREFERRED_DESTINATION);
    }

    @OnClick(R.id.button_next)
    void onNext() {

        if (spinnerCountry.getSelectedItemPosition() == 0) {
            Snackbar.make(getView(), R.string.error_option, Snackbar.LENGTH_LONG).show();
        } else {
            if (mButtonListener == null)
                mButtonListener = ((OnBoardQuestionAdapter.ButtonPressListener) getActivity());
            mButtonListener.onNextButtonPressed(MyConstants.OnBoarding.PREFERRED_DESTINATION);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void renderCountries(List<Country> countryList) {

        Logger.e(TAG, ">>> RENDERED COUNTRIES <<<");
        Logger.e(TAG, "country list: " + countryList.toString());
        dataList = new ArrayList<>();
        dataList.add(getString(R.string.question_destination));
        List<String> countries = new ArrayList<>();
        for (Country country : countryList) {
            countries.add(country.toString());
        }
        dataList.addAll(countries);
        // add a not decided
        dataList.add(getString(R.string.country_not_decided_yet));

        Logger.e(TAG, ">>> data list: " + dataList);

        DropDownAdapter adapter = new CountryDropDownAdapter(getContext(), dataList);
        spinnerCountry.setAdapter(adapter);
        spinnerCountry.setOnItemSelectedListener(this);
        if (!getTypedActivity().getPreferences().getLocation().equalsIgnoreCase(Preferences.DEFAULT_LOCATION)) {
            Logger.e(TAG, ">>> preference: " + getTypedActivity().getPreferences().getLocation());
            int index = dataList.indexOf(getTypedActivity().getPreferences().getLocation());
            if (index != -1)
                spinnerCountry.setSelection(index);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if (position != 0) {

            String previousLocation = getTypedActivity().getPreferences().getLocation();
            if (!previousLocation.equalsIgnoreCase(Preferences.DEFAULT_LOCATION)
                    && !previousLocation.equalsIgnoreCase(getString(R.string.country_not_decided_yet))) {
                Country country = Country.makeCountryFromPreference(previousLocation);
                FirebaseMessaging.getInstance().unsubscribeFromTopic(country.getTitleEnglish());
            }

            getTypedActivity().getPreferences().setLocation(dataList.get(spinnerCountry
                    .getSelectedItemPosition()));

            String currentLocation = getTypedActivity().getPreferences().getLocation();
            Country country = Country.makeCountryFromPreference(currentLocation);
            FirebaseMessaging.getInstance().subscribeToTopic(country.getTitleEnglish());

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.destroy();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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
        // do nothing
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mBroadcastReceiver);
    }
}
