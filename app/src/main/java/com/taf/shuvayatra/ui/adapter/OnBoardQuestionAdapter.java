package com.taf.shuvayatra.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.taf.shuvayatra.ui.fragment.AbroadQuestionFragment;
import com.taf.shuvayatra.ui.fragment.GenderFragment;

public class OnBoardQuestionAdapter extends FragmentPagerAdapter {
    ButtonPressListener mButtonPressListener;

    public OnBoardQuestionAdapter(FragmentManager fm,ButtonPressListener buttonPressListener) {
        super(fm);
        mButtonPressListener = buttonPressListener;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        switch(position){
            case 0 :
                fragment = AbroadQuestionFragment.newInstance(mButtonPressListener);
                break;
            case 1:
                fragment = GenderFragment.newInstance(mButtonPressListener);
                break;
            default:
                fragment = null;
                break;

        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    public interface ButtonPressListener{
        public void onNextButtonPressed(int pos);
    }
}
