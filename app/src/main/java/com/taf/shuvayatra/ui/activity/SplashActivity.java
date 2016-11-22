package com.taf.shuvayatra.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.taf.data.utils.AppPreferences;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppPreferences pref = new AppPreferences(this);

        Intent intent;
        if (!pref.getFirstLaunch()) {
            intent = new Intent(this, HomeActivity.class);
        } else {
            intent = new Intent(this, OnBoardActivity.class);
        }

        startActivity(intent);
        finish();
    }
}
