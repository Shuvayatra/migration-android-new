package com.taf.shuvayatra.ui.fragment.onboarding;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

import com.taf.data.utils.Logger;
import com.taf.shuvayatra.R;
import com.taf.shuvayatra.base.BaseActivity;
import com.taf.shuvayatra.base.BaseFragment;
import com.taf.shuvayatra.ui.adapter.DropDownAdapter;
import com.taf.shuvayatra.ui.adapter.OnBoardQuestionAdapter.ButtonPressListener;
import com.taf.util.MyConstants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    private static final String TAG = "OriginalLocationFragment";

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        String[] zones = getResources().getStringArray(R.array.zones);
        List<String> zoneList = new ArrayList<>();
        zoneList.add(getString(R.string.info_select_region));
        zoneList.addAll(Arrays.asList(zones));
        Logger.e(TAG, ">>> zone list size: " + zoneList.size());
        Logger.e(TAG, ">>> zones: " + zoneList.toString());
        DropDownAdapter adapter = new DropDownAdapter(getContext(), zoneList);
        mSpinner.setAdapter(adapter);

        mButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSpinner.getSelectedItemPosition() == 0) {
                    Snackbar.make(getView(), getString(R.string.error_option), Snackbar.LENGTH_SHORT).show();
                    return;
                }

                ((BaseActivity) getActivity()).getPreferences().setOriginalLocation(mSpinner.getSelectedItemPosition() - 1);
                if(mButtonPressListener == null) mButtonPressListener = ((ButtonPressListener) getActivity());
                mButtonPressListener.onNextButtonPressed(MyConstants.OnBoarding.ORIGINAL_LOCATION);
            }
        });

        mButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mButtonPressListener == null) mButtonPressListener = ((ButtonPressListener) getActivity());
                mButtonPressListener.onBackButtonPressed(MyConstants.OnBoarding.ORIGINAL_LOCATION);
            }
        });

    }
}
