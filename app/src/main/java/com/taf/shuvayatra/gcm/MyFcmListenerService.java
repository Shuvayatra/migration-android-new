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
import com.taf.shuvayatra.presenter.NotificationReceivedPresenter;
import com.taf.shuvayatra.ui.activity.SplashScreenActivity;

import java.util.Map;

import javax.inject.Inject;


public class MyFcmListenerService extends FirebaseMessagingService {

    @Inject
    NotificationReceivedPresenter mPresenter;

    @Override
    public void onMessageReceived(RemoteMessage pRemoteMessage) {

        initialize();

        Map<String, String> data = pRemoteMessage.getData();
        Logger.d("MyGcmListenerService_onMessageReceived", "data:" + data.toString());

        String title = data.get("title");
        String description = data.get("description");
        description = description != null ? description : "";

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

            Intent resultIntent = new Intent(this, SplashScreenActivity.class);

            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            stackBuilder.addParentStack(SplashScreenActivity.class);
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent =
                    stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(resultPendingIntent);
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context
                    .NOTIFICATION_SERVICE);
            mNotificationManager.notify(2390, builder.build());

            saveNotificationToDB(title, description);
        }
    }

    private void initialize() {
        DaggerDataComponent.builder()
                .activityModule(new ActivityModule())
                .applicationComponent(DaggerApplicationComponent.builder()
                        .applicationModule(new ApplicationModule((MyApplication) this
                                .getApplicationContext()))
                        .build())
                .dataModule(new DataModule())
                .build()
                .inject(this);
    }

    private void saveNotificationToDB(String title, String description) {
        UseCaseData useCaseData = new UseCaseData();

        Notification notification = new Notification();
        notification.setTitle(title);
        notification.setDescription(description);
        notification.setCreatedAt(System.currentTimeMillis() / 1000);
        notification.setUpdatedAt(System.currentTimeMillis() / 1000);
        useCaseData.putSerializable(UseCaseData.SUBMISSION_DATA, notification);
        mPresenter.initialize(useCaseData);
    }
}
