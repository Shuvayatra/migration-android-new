package com.taf.shuvayatra.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OnBoardQuestionAdapter extends FragmentStatePagerAdapter {

    public static final String TAG = "OnBoardQuestionAdapter";

    ButtonPressListener mButtonPressListener;
    public static final int LIST_SIZE = 6;
    /**
     * maintain a list of {@link Country}
     */
    private List<Country> countryList = new ArrayList<>();

    private List<String> fragmentTags;
    private FragmentManager manager;

    public void updateCountryList(List<Country> countryList) {
        this.countryList = countryList;
    }

    public List<Country> getCountryList() {
        return countryList;
    }

    public OnBoardQuestionAdapter(FragmentManager fm, ButtonPressListener buttonPressListener) {
        super(fm);
        manager = fm;
        fragmentTags = new ArrayList<>();
        mButtonPressListener = buttonPressListener;
    }

    @Override
    public Fragment getItem(int position) {
        Logger.e(TAG, "position: " + position);

        // TODO: 10/27/16 fix and refactor state instance for Fragments
        Fragment fragment;
        if (fragmentTags != null && manager.findFragmentByTag(fragmentTags.get(position)) != null) {
            Logger.e(TAG, ">>> found fragment by tag");
            fragment = manager.findFragmentByTag(fragmentTags.get(position));
        } else {
            Logger.e(TAG, ">>> did not find fragment by tag");
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
        }
        return fragment;
    }

    public Fragment getFragment(int position) {
        if (position >= fragmentTags.size())
            return null;
        return manager.findFragmentByTag(fragmentTags.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        if (!fragmentTags.contains(makeFragmentName(container.getId(), position))) {
            fragmentTags.add(makeFragmentName(container.getId(), position));
        }
        return super.instantiateItem(container, position);
    }


    /**
     * fragment tag maintained by the default {@link android.support.v4.app.FragmentPagerAdapter}
     * Tag are used to navigate through the different Fragments maintained by our adapter
     */
    private static String makeFragmentName(int viewId, long id) {
        return "android:switcher:" + viewId + ":" + id;
    }

    @Override
    public int getCount() {
        return LIST_SIZE;
    }

    public interface ButtonPressListener extends Serializable {

        String TAG = "button-press-listener";

        void onNextButtonPressed(int pos);

        void onBackButtonPressed(int pos);
    }
}
