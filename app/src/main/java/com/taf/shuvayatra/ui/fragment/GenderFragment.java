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

public class GenderFragment extends BaseFragment {

    public static final String TAG = "GenderFragment";
    ButtonPressListener mButtonPressListener;
    @BindView(R.id.button_next)
    Button mButtonNext;
    @BindView(R.id.radiogroup_gender)
    RadioGroup mRadioGroupGender;

    public static GenderFragment newInstance(ButtonPressListener buttonPressListener) {
        GenderFragment fragment = new GenderFragment();
        fragment.setButtonPressListener(buttonPressListener);
        return fragment;
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
    public int getLayout() {
        return R.layout.fragment_gender;
    }

    private View.OnClickListener onNextClicked(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = mRadioGroupGender.getCheckedRadioButtonId();
                if(selectedId == -1){
                    Snackbar.make(mButtonNext,"Please select an option",Snackbar.LENGTH_SHORT).show();
                    return;
                }
                RadioButton selectedButton = (RadioButton) getActivity().findViewById(selectedId);
                    ((BaseActivity) getActivity()).getPreferences().setGender(selectedButton.getText().toString());

                Logger.e(TAG," ((BaseActivity) getActivity()).getPreferences().getGender();: "+
                        ((BaseActivity) getActivity()).getPreferences().getGender());
                mButtonPressListener.onNextButtonPressed(1);
            }
        };
    }
}
