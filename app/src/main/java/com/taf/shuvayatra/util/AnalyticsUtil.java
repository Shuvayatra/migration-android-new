package com.taf.shuvayatra.util;

import android.support.annotation.Nullable;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

public class AnalyticsUtil {
    public static final String CATEGORY_FAVOURITE = "Favourite";
    public static final String CATEGORY_SHARE = "Share";
    public static final String CATEGORY_DOWNLOAD = "Download";

    public static final String ACTION_AUDIO_DOWNLOAD = "Audio";
    public static final String ACTION_BLUETOOTH = "Bluetooth";
    public static final String ACTION_FACEBOOK = "Facebook";
    public static final String ACTION_LIKE = "Like";
    public static final String ACTION_UNLIKE = "Unlike";

    public static final String LABEL_ID = "ID";

    public static void trackScreenName(Tracker pTracker, String pScreenName) {
        pTracker.setScreenName(pScreenName);
        pTracker.send(new HitBuilders.ScreenViewBuilder()
                .setNewSession()
                .build());
    }

    public static void trackEvent(Tracker pTracker, String pCategory, String
            pAction, @Nullable String pLabel, @Nullable Long pValue) {
        HitBuilders.EventBuilder eventBuilder = new HitBuilders.EventBuilder()
                .setCategory(pCategory)
                .setAction(pAction);
        if (pLabel != null) {
            eventBuilder.setLabel(pLabel);
            if (pValue != null) eventBuilder.setValue(pValue);
        }
        /*if (pUserId != null) pTracker.set("&uid", pUserId.toString());*/

        pTracker.send(eventBuilder.build());
    }


}
