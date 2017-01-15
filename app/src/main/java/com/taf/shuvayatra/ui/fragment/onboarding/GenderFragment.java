package com.taf.shuvayatra.ui.fragment.onboarding;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.taf.data.utils.Logger;
import com.taf.shuvayatra.R;
import com.taf.shuvayatra.base.BaseActivity;
import com.taf.shuvayatra.base.BaseFragment;
import com.taf.shuvayatra.ui.adapter.OnBoardQuestionAdapter.ButtonPressListener;
import com.taf.util.MyConstants;

import butterknife.BindView;

public class GenderFragment extends BaseFragment implements RadioGroup.OnCheckedChangeListener {

    public static final String TAG = "GenderFragment";
    ButtonPressListener mButtonPressListener;
    @BindView(R.id.button_next)
    Button mButtonNext;
    @BindView(R.id.button_back)
    Button mButtonBack;
    @BindView(R.id.radiogroup_gender)
    RadioGroup mRadioGroupGender;

    public static GenderFragment newInstance(ButtonPressListener buttonPressListener) {
        GenderFragment fragment = new GenderFragment();
        fragment.setButtonPressListener(buttonPressListener);
        fragment.setRetainInstance(true);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mButtonNext.setOnClickListener(onNextClicked());
        mRadioGroupGender.setOnCheckedChangeListener(this);

        String gender = ((BaseActivity) getActivity()).getPreferences().getGender();
        if (gender != null) {
            mRadioGroupGender.check(gender.equalsIgnoreCase(getString(R.string.gender_male)) ? R.id.choice_male :
                    gender.equalsIgnoreCase(getString(R.string.gender_female)) ? R.id.choice_female :
                            gender.equalsIgnoreCase(getString(R.string.gender_other)) ? R.id.choice_other : -1);
//            mRadioGroupGender.refreshDrawableState();
        }

        mButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mButtonPressListener == null) {
                    mButtonPressListener = ((ButtonPressListener) getActivity());
                }
                mButtonPressListener.onBackButtonPressed(MyConstants.OnBoarding.GENDER);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        // TODO: 11/24/16 no effect from here. fix issue later
//        final String gender = ((BaseActivity) getActivity()).getPreferences().getGender();

//        if (gender != null) {
////            mRadioGroupGender.postDelayed(new Runnable() {
////                @Override
////                public void run() {
//            mRadioGroupGender.check(gender.equalsIgnoreCase(getString(R.string.gender_male)) ? R.id.choice_male :
//                    gender.equalsIgnoreCase(getString(R.string.gender_female)) ? R.id.choice_female : -1);
//            mRadioGroupGender.refreshDrawableState();
//
////                    if (gender.equalsIgnoreCase(getString(R.string.gender_male))) {
////                        RadioButton btn = (RadioButton) mRadioGroupGender.findViewById(R.id.choice_male);
////                        btn.setChecked(true);
////
////                    } else if (gender.equalsIgnoreCase(getString(R.string.gender_female))) {
////                        RadioButton btn = (RadioButton) mRadioGroupGender.findViewById(R.id.choice_female);
////                        btn.setChecked(true);
////                    }
//            for (int i = 0; i < mRadioGroupGender.getChildCount(); i++) {
//
//                RadioButton btn = (RadioButton) mRadioGroupGender.getChildAt(i);
//                Logger.e(TAG, "text: " + btn.getText() + " value " + btn.isChecked());
//            }
////                }
////            }, 2000);
//        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        RadioButton selectedButton = (RadioButton) getActivity().findViewById(checkedId);
        ((BaseActivity) getActivity()).getPreferences().setGender(selectedButton.getText().toString());
    }

    private void setButtonPressListener(ButtonPressListener buttonPressListener) {
        mButtonPressListener = buttonPressListener;
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_gender;
    }

    private View.OnClickListener onNextClicked() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = mRadioGroupGender.getCheckedRadioButtonId();
                if (selectedId == -1) {
                    Snackbar.make(mButtonNext, getString(R.string.error_option), Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (mButtonPressListener == null)
                    mButtonPressListener = ((ButtonPressListener) getActivity());
                mButtonPressListener.onNextButtonPressed(MyConstants.OnBoarding.GENDER);
            }
        };
    }

}
