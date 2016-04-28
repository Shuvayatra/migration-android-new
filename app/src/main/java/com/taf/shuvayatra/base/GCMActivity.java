package com.taf.shuvayatra.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.taf.data.utils.Logger;
import com.taf.shuvayatra.R;
import com.taf.shuvayatra.gcm.GcmRegistrationIntentService;
import com.taf.util.MyConstants;

public abstract class GCMActivity extends AppCompatActivity {

    public static final String TAG = "GCM";
    private static final int REQUEST_CODE_RECOVER_PLAY_SERVICES = 3290;

    private BroadcastReceiver registrationBroadcastReceiver;

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(registrationBroadcastReceiver,
                new IntentFilter(MyConstants.Intent.GCM_REGISTRATION_COMPLETE));
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(registrationBroadcastReceiver);
        super.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        registrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
            }
        };

        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, GcmRegistrationIntentService.class);
            startService(intent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_RECOVER_PLAY_SERVICES:
                if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(this, getResources().getString(R.string
                            .message_play_services_not_found), Toast.LENGTH_SHORT).show();
                    finish();
                }
                return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        REQUEST_CODE_RECOVER_PLAY_SERVICES).show();
            } else {
                Logger.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

}
