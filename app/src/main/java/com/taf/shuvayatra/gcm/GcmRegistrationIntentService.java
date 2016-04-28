package com.taf.shuvayatra.gcm;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.taf.data.utils.Logger;
import com.taf.shuvayatra.R;
import com.taf.util.MyConstants;

import java.io.IOException;


public class GcmRegistrationIntentService extends IntentService {

    private static final String TAG = "GcmRegIntentService";
    private static final String[] TOPICS = {"global"};

    public GcmRegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            // Initially this call goes out to the network to retrieve the token, subsequent
            // calls are local.
            InstanceID instanceID = InstanceID.getInstance(this);
            String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            Logger.i("GcmRegistrationIntentService_onHandleIntent", "GCM Token: " + token);

            // Subscribe to topic channels
            subscribeTopics(token);
        } catch (Exception e) {
            Log.d(TAG, "Failed to complete token refresh", e);
        }
        Intent registrationComplete = new Intent(MyConstants.Intent.GCM_REGISTRATION_COMPLETE);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    private void subscribeTopics(String token) throws IOException {
        for (String topic : TOPICS) {
            GcmPubSub pubSub = GcmPubSub.getInstance(this);
            pubSub.subscribe(token, "/topics/" + topic, null);
        }
    }
}
