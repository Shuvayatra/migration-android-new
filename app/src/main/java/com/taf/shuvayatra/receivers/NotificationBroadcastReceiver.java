package com.taf.shuvayatra.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.taf.shuvayatra.MyApplication;
import com.taf.shuvayatra.ui.activity.HomeActivity;
import com.taf.shuvayatra.ui.activity.SplashActivity;

/**
 * Created by umesh on 2/9/17.
 */

public class NotificationBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if(!MyApplication.isActivityShowing){
            Intent appIntent = new Intent(context, SplashActivity.class);
            appIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(appIntent);
        }
    }
}
