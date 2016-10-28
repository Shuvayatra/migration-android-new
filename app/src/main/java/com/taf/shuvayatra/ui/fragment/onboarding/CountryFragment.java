package com.taf.shuvayatra.ui.fragment.onboarding;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.widget.Spinner;

import com.taf.data.utils.AppPreferences;
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

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * This fragment is seen in {@link OnBoardActivity} as a part of the on-boarding question and is
 * only shown if {@link OnBoardActivity#renderCountries(List)} is called.
 * <p>
 * Uses {@link AppPreferences#getCountryList()} to get the list of available countries
 *
 * @see OnBoardActivity
 */

public class CountryFragment extends BaseFragment implements CountryView {

    @BindView(R.id.spinner_country)
    Spinner spinnerCountry;
    @Inject
    CountryListPresenter presenter;

    private OnBoardQuestionAdapter.ButtonPressListener mButtonListener;

    List<String> dataList = new ArrayList<>();

    public static CountryFragment newInstance(OnBoardQuestionAdapter.ButtonPressListener listener) {
        CountryFragment fragment = new CountryFragment();
        fragment.mButtonListener = listener;
        return fragment;
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_country;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initialize();
    }

    public void initialize() {
        DaggerDataComponent.builder().dataModule(new DataModule())
                .activityModule(((BaseActivity) getActivity()).getActivityModule())
                .applicationComponent(((BaseActivity) getActivity()).getApplicationComponent())
                .build().inject(this);
        presenter.attachView(this);
        UseCaseData metaData = new UseCaseData();
        metaData.putBoolean(UseCaseData.CACHED_DATA, true);
        presenter.initialize(metaData);
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
            ((BaseActivity) getActivity()).getPreferences().setLocation(dataList.get(spinnerCountry
                    .getSelectedItemPosition()));
            if (mButtonListener == null)
                mButtonListener = ((OnBoardQuestionAdapter.ButtonPressListener) getActivity());
            mButtonListener.onNextButtonPressed(MyConstants.OnBoarding.PREFERRED_DESTINATION);
        }
    }

    @Override
    public void renderCountries(List<Country> countryList) {
        dataList.add(getString(R.string.question_destination));
        List<String> countries = new ArrayList<>();
        for (Country country : countryList) {
            countries.add(country.toString());
        }
        dataList.addAll(countries);

        DropDownAdapter adapter = new CountryDropDownAdapter(getContext(), dataList);
        spinnerCountry.setAdapter(adapter);
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

}
