package com.taf.shuvayatra.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;

import com.taf.data.utils.Logger;
import com.taf.shuvayatra.R;
import com.taf.shuvayatra.base.BaseActivity;
import com.taf.shuvayatra.ui.adapter.OnBoardQuestionAdapter;

import butterknife.BindView;

public class OnBoardActivity extends BaseActivity implements OnBoardQuestionAdapter.ButtonPressListener {

    public static final String TAG = "OnBoardActivity";
    @BindView(R.id.viewpager_questions)
    ViewPager mQuestionPager;

    @Override
    public int getLayout() {
        return R.layout.activity_on_board;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // add api request for country listing
        // api.shuvayatra.org/api/destinations

        mQuestionPager.setAdapter(new OnBoardQuestionAdapter(getSupportFragmentManager(), this));
        mQuestionPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        if (!getPreferences().getFirstLaunch()) {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onNextButtonPressed(int pos) {
        Logger.e(TAG, "pos: " + pos);
        if (pos < OnBoardQuestionAdapter.TOTAL_QUESTION_NUM - 1) {
            mQuestionPager.setCurrentItem(pos + 1);
        } else {
            getPreferences().setFirstLaunch(false);
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onBackButtonPressed(int pos) {
        Logger.e(TAG, "pos: " + pos);
        mQuestionPager.setCurrentItem(--pos);
    }
}
