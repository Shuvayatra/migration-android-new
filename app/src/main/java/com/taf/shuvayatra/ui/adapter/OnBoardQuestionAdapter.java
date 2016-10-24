package com.taf.shuvayatra.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.taf.data.utils.Logger;
import com.taf.shuvayatra.ui.fragment.onboarding.AbroadQuestionFragment;
import com.taf.shuvayatra.ui.fragment.onboarding.BirthdayFragment;
import com.taf.shuvayatra.ui.fragment.onboarding.GenderFragment;
import com.taf.shuvayatra.ui.fragment.onboarding.OriginalLocationFragment;
import com.taf.shuvayatra.ui.fragment.onboarding.UserNameFragment;
import com.taf.util.MyConstants;

public class OnBoardQuestionAdapter extends FragmentPagerAdapter {

    public static final String TAG = "OnBoardQuestionAdapter";

    ButtonPressListener mButtonPressListener;
    public static final int TOTAL_QUESTION_NUM = 5;

    public OnBoardQuestionAdapter(FragmentManager fm,ButtonPressListener buttonPressListener) {
        super(fm);
        mButtonPressListener = buttonPressListener;
    }

    @Override
    public Fragment getItem(int position) {
        Logger.e(TAG,"position: "+ position );
        Fragment fragment = null;
        switch(position){
            case MyConstants.OnBoarding.USERNAME :
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
            default:
                fragment = null;
                break;

        }
        return fragment;
    }

    @Override
    public int getCount() {
        return TOTAL_QUESTION_NUM;
    }

    public interface ButtonPressListener{
        public void onNextButtonPressed(int pos);
        public void onBackButtonPressed(int pos);
    }
}
