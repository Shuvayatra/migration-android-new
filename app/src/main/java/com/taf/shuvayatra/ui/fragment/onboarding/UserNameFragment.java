package com.taf.shuvayatra.ui.fragment.onboarding;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.taf.data.utils.Logger;
import com.taf.shuvayatra.R;
import com.taf.shuvayatra.base.BaseActivity;
import com.taf.shuvayatra.base.BaseFragment;
import com.taf.shuvayatra.ui.adapter.OnBoardQuestionAdapter.ButtonPressListener;
import com.taf.util.MyConstants;

import butterknife.BindView;

public class UserNameFragment extends BaseFragment<BaseActivity> {

    @BindView(R.id.button_next)
    Button btnNext;
    @BindView(R.id.edittext_username)
    EditText etUserName;

    private static final String TAG = "UserNameFragment";

    private ButtonPressListener mButtonPressListener;

    @Override
    public int getLayout() {
        return R.layout.fragment_user_name;
    }

    public static UserNameFragment newInstance(ButtonPressListener buttonPressListener) {
        Logger.e(TAG, ">>> user name fragment. new instance created.");
        UserNameFragment fragment = new UserNameFragment();
        fragment.setButtonPressListener(buttonPressListener);
        fragment.setRetainInstance(true);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getTypedActivity().getPreferences().getUserName() != null) {
            etUserName.setText(getTypedActivity().getPreferences().getUserName());
            etUserName.setSelection(getTypedActivity().getPreferences().getUserName().length());
        }

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etUserName.getText().toString();
                if (name.trim().isEmpty()) {
                    Snackbar.make(getView(), getString(R.string.error_name), Snackbar.LENGTH_SHORT).show();
                    return;
                }
                ((BaseActivity) getActivity()).getPreferences().setUserName(name.trim());
                if (mButtonPressListener == null) mButtonPressListener = (ButtonPressListener) getActivity();
                mButtonPressListener.onNextButtonPressed(MyConstants.OnBoarding.USERNAME);
            }
        });
    }

    private void setButtonPressListener(ButtonPressListener buttonPressListener) {
        mButtonPressListener = buttonPressListener;
    }
}
