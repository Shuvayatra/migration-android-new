package com.taf.util;

/**
 * Created by julian on 12/9/15.
 */
public class MyConstants {

    public static final String YOUTUBE_API_KEY = "AIzaSyAbqEgRif7zCL7R_OoK0rJK1Cc2LzOTWVE";

    public enum DataParent {
        COUNTRY,
        JOURNEY,
        POST
    }

    public static final class Preferences {
        public static final String PREF_NAME = "nrna_app_preferences";
        public static final String LAST_UPDATE_STAMP = "last_update_stamp";
    }

    public static final class Language {
        public static final int ENGLISH = 0;
        public static final int NEPALI = 1;
    }

    public static final class API {
        public static final String LATEST_CONTENT = "api/latest";
        public static final String DELETED_CONTENT = "api/trash";

    }

    public static final class Adapter {
        public static final int TYPE_AUDIO = 1;
        public static final int TYPE_VIDEO = 2;
        public static final int TYPE_TEXT = 3;
        public static final int TYPE_NEWS = 4;
        public static final int TYPE_JOURNEY_CATEGORY = 5;
        public static final int TYPE_PLACE = 6;
    }


    public static class Media {
        public static final String ACTION_MEDIA_BUFFER_START = "media.action.BUFFER_START";
        public static final String ACTION_MEDIA_BUFFER_STOP = "media.action.BUFFER_STOP";
        public static final String ACTION_MEDIA_COMPLETE = "media.action.COMPLETE";
        public static final String ACTION_MEDIA_CHANGE = "media.action.MEDIA_CHANGE";
        public static final String ACTION_STATUS_PREPARED = "media.action.STATUS_PREPARED";
        public static final String ACTION_SHOW_HIDE_CONTROLS = "media.action.SHOW_HIDE_CONTROLS";
        public static final String ACTION_STATUS_COMPLETION = "media.action.STATUS_COMPLETION";
        public static final String ACTION_PREFERENCE_CHANGED = "media.action.PREF_CHANGED";
        public static final String ACTION_PLAY_STATUS_CHANGE = "media.action.PLAY_STATUS_CHANGED";
    }

    public static final class Extras {
        public static final String KEY_AUDIO = "key_audio";
        public static final String KEY_VIDEO = "key_video";
        public static final String KEY_ARTICLE = "key_article";
        public static final String KEY_PLACE = "key_place";
        public static final String KEY_PLAY_STATUS = "key_play_status";
        public static final String KEY_FAVOURITES_ONLY = "key_favourites_only";
        public static final String KEY_SUBCATEGORY = "key_subcategory";
        public static final String KEY_CATEGORY = "key_category";
        public static final String KEY_FROM_CATEGORY = "key_from_category";
    }

    public static class SECTION{
        public static final String JOURNEY = "journey";
    }
}
