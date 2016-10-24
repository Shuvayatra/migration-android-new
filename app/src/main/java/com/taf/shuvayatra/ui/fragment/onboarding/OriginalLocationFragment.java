package com.taf.shuvayatra.ui.fragment.onboarding;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.taf.shuvayatra.R;
import com.taf.shuvayatra.base.BaseActivity;
import com.taf.shuvayatra.base.BaseFragment;
import com.taf.shuvayatra.ui.adapter.OnBoardQuestionAdapter.ButtonPressListener;
import com.taf.shuvayatra.ui.adapter.ZoneAdapter;
import com.taf.util.MyConstants;

import butterknife.BindView;

public class OriginalLocationFragment extends BaseFragment {

    @BindView(R.id.button_next)
    Button mButtonNext;
    @BindView(R.id.button_back)
    Button mButtonBack;
    ButtonPressListener mButtonPressListener;
    @BindView(R.id.spinner_zone)
    Spinner mSpinner;


    public static OriginalLocationFragment newInstance(ButtonPressListener buttonPressListener) {
        OriginalLocationFragment fragment = new OriginalLocationFragment();
        fragment.setButtonPressListener(buttonPressListener);
        return fragment;
    }

    private void setButtonPressListener(ButtonPressListener buttonPressListener) {
        mButtonPressListener = buttonPressListener;
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_original_location;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        String[] zones = getResources().getStringArray(R.array.zones);
        ZoneAdapter adapter = new ZoneAdapter(getContext(),zones);
        mSpinner.setAdapter(adapter);

        mButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mSpinner.getSelectedItemPosition() == 0){
                    Snackbar.make(getView(),getString(R.string.error_option), Snackbar.LENGTH_SHORT).show();
                    return;
                }

                ((BaseActivity) getActivity()).getPreferences().setOriginalLocation(mSpinner.getSelectedItemPosition()-1);
                mButtonPressListener.onNextButtonPressed(MyConstants.OnBoarding.ORIGINAL_LOCATION);
            }
        });

        mButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mButtonPressListener.onBackButtonPressed(MyConstants.OnBoarding.ORIGINAL_LOCATION);
            }
        });

    }
}
