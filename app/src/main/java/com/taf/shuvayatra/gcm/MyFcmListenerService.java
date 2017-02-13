package com.taf.shuvayatra.gcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.taf.data.utils.Logger;
import com.taf.interactor.UseCaseData;
import com.taf.model.Notification;
import com.taf.shuvayatra.MyApplication;
import com.taf.shuvayatra.R;
import com.taf.shuvayatra.di.component.DaggerApplicationComponent;
import com.taf.shuvayatra.di.component.DaggerDataComponent;
import com.taf.shuvayatra.di.module.ActivityModule;
import com.taf.shuvayatra.di.module.ApplicationModule;
import com.taf.shuvayatra.di.module.DataModule;
import com.taf.shuvayatra.presenter.deprecated.NotificationReceivedPresenter;
import com.taf.shuvayatra.receivers.NotificationBroadcastReceiver;
import com.taf.shuvayatra.ui.activity.SplashActivity;
import com.taf.shuvayatra.ui.deprecated.activity.SplashScreenActivity;
import com.taf.util.MyConstants;

import java.util.Map;

import javax.inject.Inject;


public class MyFcmListenerService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage pRemoteMessage) {

        Map<String, String> data = pRemoteMessage.getData();

        String title = data.get("title");
        String description = data.get("description");
        String deeplink = data.get("deeplink");
        description = (description != null ? description : "no description");

        if (title != null) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.ic_notification)
                    .setContentTitle(title)
                    .setContentText(description)
                    .setDefaults(NotificationCompat.DEFAULT_SOUND | NotificationCompat
                            .DEFAULT_VIBRATE)
                    .setLights(Color.GREEN, 2000, 3000)
                    .setAutoCancel(true)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(description));

            Intent resultIntent = new Intent(MyConstants.Intent.ACTION_CLICK_PUSH_NOTIFICATION);
            if (deeplink != null) {
                resultIntent.putExtra("deeplink", deeplink);
            }

            PendingIntent resultPendingIntent =
                    PendingIntent.getBroadcast(getBaseContext(), 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(resultPendingIntent);
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context
                    .NOTIFICATION_SERVICE);
            mNotificationManager.notify(2390, builder.build());
        }
    }
}
