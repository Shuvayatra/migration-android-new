package com.taf.shuvayatra.ui.fragment.onboarding;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.taf.data.utils.AppPreferences;
import com.taf.model.Country;
import com.taf.shuvayatra.R;
import com.taf.shuvayatra.base.BaseActivity;
import com.taf.shuvayatra.base.BaseFragment;
import com.taf.shuvayatra.ui.activity.OnBoardActivity;
import com.taf.shuvayatra.ui.adapter.DropDownAdapter;
import com.taf.shuvayatra.ui.adapter.OnBoardQuestionAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

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
    private boolean isInvalid = true;
    private String selectedCountry;

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

        final List<String> dataList = new ArrayList<>();
        dataList.add(getString(R.string.question_destination));
        dataList.addAll(new ArrayList<>(((BaseActivity) getActivity())
                .getPreferences().getCountryList()));

        DropDownAdapter adapter = new CountryDropDownAdapter(getContext(), dataList);
        spinnerCountry.setAdapter(adapter);
        spinnerCountry.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long itemId) {

                isInvalid = position != 0;
                selectedCountry = dataList.get(position);
            }
        });
    }

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
