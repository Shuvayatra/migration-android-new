package com.taf.shuvayatra.ui.fragment.onboarding;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.widget.Spinner;

import com.taf.data.utils.AppPreferences;
import com.taf.shuvayatra.R;
import com.taf.shuvayatra.base.BaseActivity;
import com.taf.shuvayatra.base.BaseFragment;
import com.taf.shuvayatra.ui.activity.OnBoardActivity;
import com.taf.shuvayatra.ui.adapter.DropDownAdapter;
import com.taf.shuvayatra.ui.adapter.OnBoardQuestionAdapter;
import com.taf.util.MyConstants;

import java.util.ArrayList;
import java.util.List;

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

public class CountryFragment extends BaseFragment {

    @BindView(R.id.spinner_country)
    Spinner spinnerCountry;

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

        dataList.add(getString(R.string.question_destination));
        dataList.addAll(new ArrayList<>(((BaseActivity) getActivity())
                .getPreferences().getCountryList()));

        DropDownAdapter adapter = new CountryDropDownAdapter(getContext(), dataList);
        spinnerCountry.setAdapter(adapter);
    }

    @OnClick(R.id.button_back)
    void onBack() {
        mButtonListener.onBackButtonPressed(MyConstants.OnBoarding.PREFERRED_DESTINATION);
    }

    @OnClick(R.id.button_next)
    void onNext() {

        if (spinnerCountry.getSelectedItemPosition() == 0) {
            Snackbar.make(getView(), R.string.error_option, Snackbar.LENGTH_LONG).show();
        } else {
            ((BaseActivity) getActivity()).getPreferences().setLocation(dataList.get(spinnerCountry
                    .getSelectedItemPosition()));
            mButtonListener.onNextButtonPressed(MyConstants.OnBoarding.PREFERRED_DESTINATION);
        }
    }

    private static final String TAG = "CountryFragment";

    public class CountryDropDownAdapter extends DropDownAdapter {

        public CountryDropDownAdapter(Context context, List<String> dataList) {
            super(context, dataList);
        }

        @Override
        public String getSpinnerText(int position) {
            if (position == 0)
                return super.getSpinnerText(position);
            else
                return getData().get(position).split(",")[1];
        }
    }

}
