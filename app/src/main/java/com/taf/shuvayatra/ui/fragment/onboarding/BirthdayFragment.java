package com.taf.shuvayatra.ui.fragment.onboarding;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.taf.shuvayatra.R;
import com.taf.shuvayatra.base.BaseActivity;
import com.taf.shuvayatra.base.BaseFragment;
import com.taf.shuvayatra.ui.adapter.OnBoardQuestionAdapter.ButtonPressListener;
import com.taf.util.MyConstants;

import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;

public class BirthdayFragment extends BaseFragment {

    @BindView(R.id.button_next)
    Button mButtonNext;
    @BindView(R.id.button_back)
    Button mButtonBack;
    @BindView(R.id.textview_birthday)
    TextView mTextViewBirthday;
    ButtonPressListener mButtonPressListener;
    Calendar birthday;

    @Override
    public int getLayout() {
        return R.layout.fragment_birthday;
    }

    public static BirthdayFragment newInstance(ButtonPressListener buttonPressListener) {
        BirthdayFragment fragment = new BirthdayFragment();
        fragment.setButtonPressListener(buttonPressListener);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (birthday == null) {
                    Snackbar.make(getView(), getString(R.string.error_date), Snackbar.LENGTH_SHORT).show();
                    return;
                }
                ((BaseActivity) getActivity()).getPreferences().setBirthday(birthday.getTimeInMillis());
                mButtonPressListener.onNextButtonPressed(MyConstants.OnBoarding.BIRTHDAY);
            }
        });

        mButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mButtonPressListener.onBackButtonPressed(MyConstants.OnBoarding.BIRTHDAY);
            }
        });

        mTextViewBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog dialog = new DatePickerDialog(getContext(),

                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                if (birthday == null) birthday = Calendar.getInstance();
                                birthday.set(Calendar.YEAR, year);
                                birthday.set(Calendar.MONTH, month);
                                birthday.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                mTextViewBirthday.setText(year + " / " + birthday.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()) + " / " + dayOfMonth);
                            }
                        },

                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                );

                dialog.show();
            }
        });

        if (birthday != null) {
            int year = birthday.get(Calendar.YEAR);
//            int month = birthday.get(Calendar.MONTH);
            int dayOfMonth = birthday.get(Calendar.DAY_OF_MONTH);
            mTextViewBirthday.setText(year + " / " + birthday.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()) + " / " + dayOfMonth);
        }
    }

    private void setButtonPressListener(ButtonPressListener buttonPressListener) {
        mButtonPressListener = buttonPressListener;
    }
}
