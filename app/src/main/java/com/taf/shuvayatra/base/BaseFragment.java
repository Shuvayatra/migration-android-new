package com.taf.shuvayatra.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.taf.interactor.UseCaseData;
import com.taf.model.Country;
import com.taf.shuvayatra.R;
import com.taf.util.MyConstants;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseFragment<T extends BaseActivity> extends Fragment {

    public Unbinder mUnbinder;

    public abstract int getLayout();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public UseCaseData getUserCredentialsUseCase() {
        UseCaseData useCaseData = new UseCaseData();
        String gender = ((BaseActivity) getActivity()).getPreferences().getGender();
        if (gender != null)
            if (gender.equalsIgnoreCase(getString(R.string.gender_male))) {
                useCaseData.putString(UseCaseData.USER_GENDER, "M");
            } else if (gender.equalsIgnoreCase(getString(R.string.gender_female))) {
                useCaseData.putString(UseCaseData.USER_GENDER, "F");
            } else if (gender.equalsIgnoreCase(getString(R.string.gender_other))) {
                useCaseData.putString(UseCaseData.USER_GENDER, "O");
            }
        String location = ((BaseActivity) getActivity()).getPreferences().getLocation();
        useCaseData.putString(UseCaseData.COUNTRY_ID, location.equalsIgnoreCase(MyConstants
                .Preferences.DEFAULT_LOCATION) || location.equalsIgnoreCase(
                getString(R.string.country_not_decided_yet)) ? null : location.split(",")[Country.INDEX_ID]);
        return useCaseData;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        return inflater.inflate(getLayout(), container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mUnbinder = ButterKnife.bind(this, view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public T getTypedActivity() {
        return (T) getActivity();
    }
}
