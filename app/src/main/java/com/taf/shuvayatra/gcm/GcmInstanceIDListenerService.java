package com.taf.shuvayatra.gcm;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.taf.data.utils.Logger;


public class GcmInstanceIDListenerService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Logger.d("GcmInstanceIDListenerService_onTokenRefresh", "token: " + refreshedToken);
    }
}