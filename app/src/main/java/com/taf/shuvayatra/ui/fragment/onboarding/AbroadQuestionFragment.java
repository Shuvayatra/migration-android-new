package com.taf.shuvayatra.ui.fragment.onboarding;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import com.taf.data.utils.Logger;
import com.taf.shuvayatra.R;
import com.taf.shuvayatra.base.BaseActivity;
import com.taf.shuvayatra.base.BaseFragment;
import com.taf.shuvayatra.ui.adapter.OnBoardQuestionAdapter.ButtonPressListener;
import com.taf.util.MyConstants;

import butterknife.BindView;

public class AbroadQuestionFragment extends BaseFragment implements View.OnClickListener {
    public static final String TAG = "AbroadQuestionFragment";
    ButtonPressListener mButtonPressListener;
    @BindView(R.id.button_next)
    public Button mButtonNext;
    @BindView(R.id.button_back)
    Button mButtonBack;
    @BindView(R.id.radiogroup_previous_work_status)
    RadioGroup mRadioGroupPreviousWorkStatus;

    public static AbroadQuestionFragment newInstance(ButtonPressListener buttonPressListener) {
        AbroadQuestionFragment fragment = new AbroadQuestionFragment();
        fragment.setButtonPressListener(buttonPressListener);
        return fragment;
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_abroad_question;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mButtonNext.setOnClickListener(onNextClicked());

        if (((BaseActivity) getActivity()).getPreferences().isOnBoardingCountryListLoaded())
            mButtonNext.setText(getString(R.string.next));

        mButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mButtonPressListener == null) mButtonPressListener = ((ButtonPressListener) getActivity());
                mButtonPressListener.onBackButtonPressed(MyConstants.OnBoarding.WORK_STATUS);
            }
        });
    }

    private void setButtonPressListener(ButtonPressListener buttonPressListener) {
        mButtonPressListener = buttonPressListener;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.button_next:
                onNextClicked();
        }
    }

    private View.OnClickListener onNextClicked() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = mRadioGroupPreviousWorkStatus.getCheckedRadioButtonId();
                if (selectedId == -1) {
                    Snackbar.make(mButtonNext, getString(R.string.error_option), Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (selectedId == R.id.rb_working) {
                    ((BaseActivity) getActivity()).getPreferences()
                            .setPreviousWorkStatus(0);
                } else if (selectedId == R.id.rb_back_from_abroad) {
                    ((BaseActivity) getActivity()).getPreferences()
                            .setPreviousWorkStatus(1);
                } else if (selectedId == R.id.rb_planing) {
                    ((BaseActivity) getActivity()).getPreferences()
                            .setPreviousWorkStatus(2);
                } else if (selectedId == R.id.rb_not_going) {
                    ((BaseActivity) getActivity()).getPreferences()
                            .setPreviousWorkStatus(3);
                }

                Logger.e(TAG, " ((BaseActivity) getActivity()).getPreferences().getPreviousWorkStatus();: " +
                        ((BaseActivity) getActivity()).getPreferences().getPreviousWorkStatus());
                if(mButtonPressListener == null) mButtonPressListener = ((ButtonPressListener) getActivity());
                mButtonPressListener.onNextButtonPressed(MyConstants.OnBoarding.WORK_STATUS);
            }
        };
    }
}
