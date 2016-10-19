package com.taf.shuvayatra.ui.fragment;

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

import butterknife.BindView;

public class AbroadQuestionFragment extends BaseFragment implements View.OnClickListener {
    public static final String TAG = "AbroadQuestionFragment";
    ButtonPressListener mButtonPressListener;
    @BindView(R.id.button_next)
    Button mButtonNext;
    @BindView(R.id.radiogroup_previous_work_status)
    RadioGroup mRadioGroupPreviousWorkStatus;

    public static AbroadQuestionFragment newInstance(ButtonPressListener buttonPressListener){
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
    }

    private void setButtonPressListener(ButtonPressListener buttonPressListener) {
        mButtonPressListener = buttonPressListener;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.button_next:
                onNextClicked();


        }
    }

    private View.OnClickListener onNextClicked(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = mRadioGroupPreviousWorkStatus.getCheckedRadioButtonId();
                if(selectedId == -1){
                    Snackbar.make(mButtonNext,getString(R.string.error_onboard),Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if(selectedId == R.id.radiobutton_yes) {
                    ((BaseActivity) getActivity()).getPreferences().setPreviousWorkStatus(true);
                } else {
                    ((BaseActivity) getActivity()).getPreferences().setPreviousWorkStatus(false);
                }
                Logger.e(TAG," ((BaseActivity) getActivity()).getPreferences().getPreviousWorkStatus();: "+
                        ((BaseActivity) getActivity()).getPreferences().getPreviousWorkStatus());
                mButtonPressListener.onNextButtonPressed(0);
            }
        };
    }
}
