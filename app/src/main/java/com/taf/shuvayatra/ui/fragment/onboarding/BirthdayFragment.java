package com.taf.shuvayatra.ui.fragment.onboarding;

import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.taf.data.utils.Logger;
import com.taf.shuvayatra.R;
import com.taf.shuvayatra.base.BaseActivity;
import com.taf.shuvayatra.base.BaseFragment;
import com.taf.shuvayatra.ui.adapter.OnBoardQuestionAdapter.ButtonPressListener;
import com.taf.util.MyConstants;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;

public class BirthdayFragment extends BaseFragment implements DatePickerDialog.OnDateSetListener {

    @BindView(R.id.button_next)
    Button mButtonNext;
    @BindView(R.id.button_back)
    Button mButtonBack;
    @BindView(R.id.textview_birthday)
    TextView mTextViewBirthday;

    private ButtonPressListener mButtonPressListener;
    private Calendar birthday;

    private static final String TAG = "BirthdayFragment";

    @Override
    public int getLayout() {
        return R.layout.fragment_birthday;
    }

    public static BirthdayFragment newInstance(ButtonPressListener buttonPressListener) {
        BirthdayFragment fragment = new BirthdayFragment();
        fragment.setButtonPressListener(buttonPressListener);
        fragment.setRetainInstance(true);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        long savedBirthday = ((BaseActivity) getActivity()).getPreferences().getBirthday();
        // to correct the date picker format
        Locale.setDefault(Locale.ENGLISH);
        if (savedBirthday != Long.MIN_VALUE) {

            birthday = Calendar.getInstance(Locale.ENGLISH);
            birthday.setTime(new Date(savedBirthday));
            mTextViewBirthday.setText(String.format(
                    Locale.getDefault(),
                    "%d/%s/%d",
                    birthday.get(Calendar.YEAR),
                    birthday.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US),
                    birthday.get(Calendar.DAY_OF_MONTH)));
        }

        mButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (birthday == null) {
                    Snackbar.make(getView(), getString(R.string.error_date), Snackbar.LENGTH_SHORT).show();
                    return;
                }
                ((BaseActivity) getActivity()).getPreferences().setBirthday(birthday.getTimeInMillis());
                if (mButtonPressListener == null)
                    mButtonPressListener = ((ButtonPressListener) getActivity());
                mButtonPressListener.onNextButtonPressed(MyConstants.OnBoarding.BIRTHDAY);
            }
        });

        mButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mButtonPressListener == null)
                    mButtonPressListener = ((ButtonPressListener) getActivity());
                mButtonPressListener.onBackButtonPressed(MyConstants.OnBoarding.BIRTHDAY);
            }
        });

        mTextViewBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (birthday == null) birthday = Calendar.getInstance();
                DatePickerDialog dialog = new DatePickerDialog(getActivity(),
                        BirthdayFragment.this,
                        birthday.get(Calendar.YEAR),
                        birthday.get(Calendar.MONTH),
                        birthday.get(Calendar.DAY_OF_MONTH)
                );
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                    dialog.getDatePicker().setSpinnersShown(true);
                    dialog.getDatePicker().setCalendarViewShown(false);
                }
                dialog.show();
            }
        });
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        // update birthday reference
        birthday.set(year, month, dayOfMonth);
        // update preference and view
        ((BaseActivity) getActivity()).getPreferences().setBirthday(birthday.getTimeInMillis());
        mTextViewBirthday.setText(String.format(Locale.getDefault(),
                "%d/%s/%d", year, birthday.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US),
                dayOfMonth));
    }

    private void setButtonPressListener(ButtonPressListener buttonPressListener) {
        mButtonPressListener = buttonPressListener;
    }
}
