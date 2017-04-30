package com.taf.shuvayatra.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.taf.shuvayatra.MyApplication;
import com.taf.shuvayatra.ui.activity.HomeActivity;
import com.taf.shuvayatra.ui.activity.SplashActivity;
import com.taf.util.MyConstants;

/**
 * Created by umesh on 2/9/17.
 */

public class NotificationBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equalsIgnoreCase(MyConstants.Intent.ACTION_CLICK_PUSH_NOTIFICATION)) {
            String deeplink = (String) intent.getExtras().get("deeplink");
            Intent appIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(deeplink));
            appIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(appIntent);
        } else if (intent.getAction().equalsIgnoreCase(MyConstants.Intent.ACTION_CLICK_PLAYER)) {
            if (!MyApplication.isActivityShowing) {
                Intent appIntent = new Intent(context, SplashActivity.class);
                appIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(appIntent);
            }
        }
    }
}
