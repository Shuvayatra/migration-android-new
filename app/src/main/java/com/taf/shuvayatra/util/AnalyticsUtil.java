package com.taf.shuvayatra.util;

import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

public class AnalyticsUtil {

    public static void logAppOpenEvent(FirebaseAnalytics analytics) {
        analytics.logEvent(FirebaseAnalytics.Event.APP_OPEN, null);
    }

    public static void logViewEvent(FirebaseAnalytics analytics, Long id, String title, String
            type) {
        logEvent(analytics, FirebaseAnalytics.Event.VIEW_ITEM, id, title, type);
    }

    public static void logBluetoothShareEvent(FirebaseAnalytics analytics, Long id, String title,
                                              String type) {
        logEvent(analytics, "bluetooth_share_item", id, title, type);
    }

    public static void logDownloadEvent(FirebaseAnalytics analytics, Long id, String title,
                                        String type) {
        logEvent(analytics, "download_item", id, title, type);
    }

    private static void logEvent(FirebaseAnalytics analytics, String eventType, Long id, String
            title, String type) {
        Bundle analyticsData = new Bundle();
        analyticsData.putString(FirebaseAnalytics.Param.ITEM_ID, id.toString());
        analyticsData.putString(FirebaseAnalytics.Param.ITEM_NAME, title);
        analyticsData.putString(FirebaseAnalytics.Param.CONTENT_TYPE, type);
        analytics.logEvent(eventType, analyticsData);
    }

    public static void logSearchEvent(FirebaseAnalytics analytics, String query, boolean byTag) {
        Bundle analyticsData = new Bundle();
        analyticsData.putString(FirebaseAnalytics.Param.SEARCH_TERM, query);
        analyticsData.putBoolean("search_by_tag", byTag);
        analytics.logEvent(FirebaseAnalytics.Event.SEARCH, analyticsData);
    }

    public static void logFavouriteEvent(FirebaseAnalytics analytics, Long id, String title,
                                         String type, boolean status) {
        Bundle analyticsData = new Bundle();
        analyticsData.putString(FirebaseAnalytics.Param.ITEM_ID, id.toString());
        analyticsData.putString(FirebaseAnalytics.Param.ITEM_NAME, title);
        analyticsData.putString(FirebaseAnalytics.Param.CONTENT_TYPE, type);
        analyticsData.putBoolean("favourte_status", status);
        analytics.logEvent("favourite_item", analyticsData);
    }

}
