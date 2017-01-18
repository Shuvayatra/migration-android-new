package com.taf.shuvayatra.ui.fragment.onboarding;

import android.databinding.Bindable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;

import com.taf.data.utils.Logger;
import com.taf.shuvayatra.R;
import com.taf.shuvayatra.base.BaseActivity;
import com.taf.shuvayatra.base.BaseFragment;
import com.taf.shuvayatra.ui.adapter.OnBoardQuestionAdapter.ButtonPressListener;
import com.taf.util.MyConstants;

import butterknife.BindView;

public class AbroadQuestionFragment extends BaseFragment implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    public static final String TAG = "AbroadQuestionFragment";
    ButtonPressListener mButtonPressListener;
    @BindView(R.id.button_next)
    public ImageView mButtonNext;
    @BindView(R.id.button_back)
    ImageView mButtonBack;
    @BindView(R.id.radiogroup_previous_work_status)
    RadioGroup mRadioGroupPreviousWorkStatus;
    @BindView(R.id.scroll_view)
    ScrollView mScrollContainer;

    public static AbroadQuestionFragment newInstance(ButtonPressListener buttonPressListener) {
        AbroadQuestionFragment fragment = new AbroadQuestionFragment();
        fragment.setButtonPressListener(buttonPressListener);
        fragment.setRetainInstance(true);
        return fragment;
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_abroad_question;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mRadioGroupPreviousWorkStatus.setOnCheckedChangeListener(this);
        mButtonNext.setOnClickListener(onNextClicked());

        if (((BaseActivity) getActivity()).getPreferences().getPreviousWorkStatus() != null) {
            String workStatus = ((BaseActivity) getActivity()).getPreferences().getPreviousWorkStatus();
            mRadioGroupPreviousWorkStatus.check(workStatus.equalsIgnoreCase(getString(R.string.work_status_back_from_abroad)) ? R.id.rb_back_from_abroad :
                    workStatus.equalsIgnoreCase(getString(R.string.work_status_planning)) ? R.id.rb_planning :
                            workStatus.equalsIgnoreCase(getString(R.string.work_status_not_going)) ? R.id.rb_not_going :
                                    workStatus.equalsIgnoreCase(getString(R.string.work_status_working_abroad)) ? R.id.rb_working : -1);
        }

        if (((BaseActivity) getActivity()).getPreferences().isOnBoardingCountryListLoaded())
//            mButtonNext.setText(getString(R.string.next));

        mButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mButtonPressListener == null)
                    mButtonPressListener = ((ButtonPressListener) getActivity());
                mButtonPressListener.onBackButtonPressed(MyConstants.OnBoarding.WORK_STATUS);
            }
        });
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        RadioButton selectedButton = (RadioButton) getActivity().findViewById(checkedId);
        ((BaseActivity) getActivity()).getPreferences()
                .setPreviousWorkStatus(selectedButton.getText().toString());
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

                Logger.e(TAG, " ((BaseActivity) getActivity()).getPreferences().getPreviousWorkStatus();: " +
                        ((BaseActivity) getActivity()).getPreferences().getPreviousWorkStatus());
                if (mButtonPressListener == null)
                    mButtonPressListener = ((ButtonPressListener) getActivity());
                mButtonPressListener.onNextButtonPressed(MyConstants.OnBoarding.WORK_STATUS);
            }
        };
    }
}
