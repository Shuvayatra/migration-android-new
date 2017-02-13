package com.taf.shuvayatra.util;

import android.app.Activity;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

public class AnalyticsUtil {

    public static final String EVENT_BLOCK_SELECTION = "block_selection";
    public static final String EVENT_AUDIO_BLUETOOTH_SHARE = "post_audio_share_bluetooth";
    public static final String EVENT_DOWNLOAD_AUDIO = "download_audio";
    public static final String EVENT_POST_FAVORITE = "favorite_post";
    public static final String EVENT_READ_MORE = "read_more";
    public static final String EVENT_RADIO_ENGAGEMENT = "radio_engagement";

    public static final String KEY_SEARCH_CONTENT_TYPE = "search_content_type";
    public static final String KEY_FAVORITE_STATUS = "favorite_status";

    public static void logScreenTime(FirebaseAnalytics analytics, Activity activity, String screenName) {
        analytics.setCurrentScreen(activity, screenName, null);
    }

    public static void logAppOpenEvent(FirebaseAnalytics analytics) {
        analytics.logEvent(FirebaseAnalytics.Event.APP_OPEN, null);
    }

    public static void logViewEvent(FirebaseAnalytics analytics, Long id, String title, String type) {
        logEvent(analytics, FirebaseAnalytics.Event.VIEW_ITEM, id, title, type);
    }

    public static void logRadioViewEvent(FirebaseAnalytics analytics, Long id, String title) {
        logEvent(analytics, EVENT_RADIO_ENGAGEMENT, id, title);
    }

    public static void logBlockEvent(FirebaseAnalytics analytics, String blockName,
                                     String postName, String screen, String blockType) {
        logEvent(analytics, EVENT_BLOCK_SELECTION, blockName, postName, screen, blockType);
    }

    public static void logBluetoothShareEvent(FirebaseAnalytics analytics, Long id, String title,
                                              String type) {
        logEvent(analytics, EVENT_AUDIO_BLUETOOTH_SHARE, id, title, type);
    }

    public static void logShareEvent(FirebaseAnalytics analytics, Long id, String title, String
            type) {
        logEvent(analytics, FirebaseAnalytics.Event.SHARE, id, title, type);
    }

    public static void logDownloadEvent(FirebaseAnalytics analytics, Long id, String title,
                                        String type) {
        logEvent(analytics, EVENT_DOWNLOAD_AUDIO, id, title, type);
    }

    private static void logEvent(FirebaseAnalytics analytics, String eventType, String blockName,
                                 String postName, String screen, String blockType) {
        Bundle analyticsData = new Bundle();
        // block name
        analyticsData.putString(FirebaseAnalytics.Param.ITEM_ID, blockName);
        // screen
        analyticsData.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, screen);
        // post name
        analyticsData.putString(FirebaseAnalytics.Param.ITEM_NAME, postName);
        // list or slider
        analyticsData.putString(FirebaseAnalytics.Param.CONTENT_TYPE, blockType);
        analytics.logEvent(eventType, analyticsData);
    }

    public static void logReadMoreEvent(FirebaseAnalytics analytics, long blockId,
                                        String blockName, String screen, String blockType) {
        Bundle analyticsData = new Bundle();
        // block name
        analyticsData.putString(FirebaseAnalytics.Param.ITEM_ID, String.valueOf(blockId));
        // screen
        analyticsData.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, screen);
        // post name
        analyticsData.putString(FirebaseAnalytics.Param.ITEM_NAME, blockName);
        // list or slider
        analyticsData.putString(FirebaseAnalytics.Param.CONTENT_TYPE, blockType);
        analytics.logEvent(EVENT_READ_MORE, analyticsData);
    }

    private static void logEvent(FirebaseAnalytics analytics, String eventType, Long id, String
            title, String type) {
        Bundle analyticsData = new Bundle();
        analyticsData.putString(FirebaseAnalytics.Param.ITEM_ID, id.toString());
        analyticsData.putString(FirebaseAnalytics.Param.ITEM_NAME, title);
        analyticsData.putString(FirebaseAnalytics.Param.CONTENT_TYPE, type);
        analytics.logEvent(eventType, analyticsData);
    }

    private static void logEvent(FirebaseAnalytics analytics, String eventType, Long id, String
            title) {
        Bundle analyticsData = new Bundle();
        analyticsData.putString(FirebaseAnalytics.Param.ITEM_ID, id.toString());
        analyticsData.putString(FirebaseAnalytics.Param.ITEM_NAME, title);
        analytics.logEvent(eventType, analyticsData);
    }

    public static void logSearchEvent(FirebaseAnalytics analytics, String query, String contentType) {
        Bundle analyticsData = new Bundle();
        analyticsData.putString(FirebaseAnalytics.Param.SEARCH_TERM, query);
        analyticsData.putString(KEY_SEARCH_CONTENT_TYPE, contentType);
        analytics.logEvent(FirebaseAnalytics.Event.SEARCH, analyticsData);
    }

    public static void logFavouriteEvent(FirebaseAnalytics analytics, Long id, String title,
                                         String type, boolean status) {
        Bundle analyticsData = new Bundle();
        analyticsData.putString(FirebaseAnalytics.Param.ITEM_ID, id.toString());
        analyticsData.putString(FirebaseAnalytics.Param.ITEM_NAME, title);
        analyticsData.putString(FirebaseAnalytics.Param.CONTENT_TYPE, type);
        analyticsData.putBoolean(KEY_FAVORITE_STATUS, status);
        analytics.logEvent(EVENT_POST_FAVORITE, analyticsData);
    }

}
