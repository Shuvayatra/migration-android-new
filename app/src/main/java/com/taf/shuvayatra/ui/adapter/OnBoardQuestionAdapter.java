package com.taf.shuvayatra.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.taf.data.utils.Logger;
import com.taf.model.Country;
import com.taf.shuvayatra.ui.fragment.onboarding.AbroadQuestionFragment;
import com.taf.shuvayatra.ui.fragment.onboarding.BirthdayFragment;
import com.taf.shuvayatra.ui.fragment.onboarding.CountryFragment;
import com.taf.shuvayatra.ui.fragment.onboarding.GenderFragment;
import com.taf.shuvayatra.ui.fragment.onboarding.OriginalLocationFragment;
import com.taf.shuvayatra.ui.fragment.onboarding.UserNameFragment;
import com.taf.util.MyConstants;

import java.util.ArrayList;
import java.util.List;

public class OnBoardQuestionAdapter extends FragmentPagerAdapter {

    public static final String TAG = "OnBoardQuestionAdapter";

    ButtonPressListener mButtonPressListener;
    public static final int LIST_SIZE = 6;
    /**
     * maintain a list of {@link Country}
     */
    private List<Country> countryList = new ArrayList<>();

    // saving fragments
    SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();

    public void updateCountryList(List<Country> countryList) {
        this.countryList = countryList;
    }

    public List<Country> getCountryList() {
        return countryList;
    }

    public OnBoardQuestionAdapter(FragmentManager fm, ButtonPressListener buttonPressListener) {
        super(fm);
        mButtonPressListener = buttonPressListener;
    }

    @Override
    public Fragment getItem(int position) {
        Logger.e(TAG, "position: " + position);
        Fragment fragment;
        switch (position) {
            case MyConstants.OnBoarding.USERNAME:
                fragment = UserNameFragment.newInstance(mButtonPressListener);
                break;
            case MyConstants.OnBoarding.GENDER:
                fragment = GenderFragment.newInstance(mButtonPressListener);
                break;
            case MyConstants.OnBoarding.BIRTHDAY:
                fragment = BirthdayFragment.newInstance(mButtonPressListener);
                break;
            case MyConstants.OnBoarding.ORIGINAL_LOCATION:
                fragment = OriginalLocationFragment.newInstance(mButtonPressListener);
                break;
            case MyConstants.OnBoarding.WORK_STATUS:
                fragment = AbroadQuestionFragment.newInstance(mButtonPressListener);
                break;
            case MyConstants.OnBoarding.PREFERRED_DESTINATION:
                fragment = CountryFragment.newInstance(mButtonPressListener);
                break;
            default:
                fragment = null;
                break;

        }
        return fragment;
    }

    public Fragment getFragment(int position) {
        return registeredFragments.get(position);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    @Override
    public int getCount() {
        return LIST_SIZE;
    }

    public interface ButtonPressListener {

        void onNextButtonPressed(int pos);

        void onBackButtonPressed(int pos);
    }
}
