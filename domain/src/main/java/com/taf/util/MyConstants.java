package com.taf.util;

/**
 * Created by julian on 12/9/15.
 */
public class MyConstants {

    public static final String YOUTUBE_API_KEY = "AIzaSyAbqEgRif7zCL7R_OoK0rJK1Cc2LzOTWVE";

    public enum DataParent {
        COUNTRY,
        QUESTION,
        ANSWER,
        POST
    }

    ;

    public enum Stage {
        PRE_APPLICATION,
        APPLICATION,
        WORK_PERMIT,
        DESTINATION_COUNTRY;

        public static String toString(Stage pStage) {
            if (pStage != null)
                switch (pStage) {
                    default:
                    case PRE_APPLICATION:
                        return DataInfo.STAGE_PRE_APPLICATION;
                    case APPLICATION:
                        return DataInfo.STAGE_APPLICATION;
                    case WORK_PERMIT:
                        return DataInfo.STAGE_WORK_PERMIT;
                    case DESTINATION_COUNTRY:
                        return DataInfo.STAGE_DESTINATION_COUNTRY;
                }
            else
                return null;
        }
    }

    public enum PostType {
        AUDIO,
        VIDEO,
        TEXT;

        public static String toString(PostType pType) {
            if (pType != null)
                switch (pType) {
                    default:
                    case AUDIO:
                        return DataInfo.TYPE_AUDIO;
                    case VIDEO:
                        return DataInfo.TYPE_VIDEO;
                    case TEXT:
                        return DataInfo.TYPE_TEXT;
                }
            else
                return null;
        }
    }

    public static final class Preferences {
        public static final String PREF_NAME = "nrna_app_preferences";

        public static final String LAST_UPDATE_STAMP = "last_update_stamp";

        public static final String TAG_FILTER_CHOICES = "tag_filter_choices";
        public static final String STAGE_FILTER_CHOICES = "stage_filter_choices";

        public static final String LANGUAGE = "language";
        public static final String GENDER = "gender";
        public static final String COUNTRY = "country";
        public static final String USERTYPE = "user_type";
        public static final String FIRST_TIME = "first_time";

        public static final String DOWNLOAD_REFERENCES = "download_references";
    }

    public static final class ShowCase{
        public static final Long MainActivity = 1L;
        public static final Long AudioActivity = 2L;
    }

    public static final class Language {
        public static final int ENGLISH = 0;
        public static final int NEPALI = 1;
    }

    public static final class API {
        public static final String LATEST_CONTENT = "api/latest";
        public static final String DELETED_CONTENT = "api/trash";
        public static final String USER_PREFERENCES = "api/latest";

    }

    public static class DataInfo {
        public static final String STAGE_PRE_APPLICATION = "pre-application";
        public static final String STAGE_APPLICATION = "applying";
        public static final String STAGE_WORK_PERMIT = "work-permit";
        public static final String STAGE_DESTINATION_COUNTRY = "destination-country";

        public static final String TYPE_AUDIO = "audio";
        public static final String TYPE_VIDEO = "video";
        public static final String TYPE_TEXT = "text";
    }

    public static final class Adapter {
        public static final int TYPE_AUDIO = 1;
        public static final int TYPE_VIDEO = 2;
        public static final int TYPE_TEXT = 3;
        public static final int TYPE_NEWS = 4;
        public static final int TYPE_JOURNEY_CATEGORY = 5;
    }

    public static final class VideoAdapterType {
        public static final int TYPE_LIST = 0;
        public static final int TYPE_GRID = 1;
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
        public static final String KEY_DOWNLOAD_STATUS = "key_download_status";
        public static final String KEY_QUESTION = "key_question";
        public static final String KEY_Country = "key_country";
        public static final String KEY_TYPE = "key_type";
        public static final String KEY_AUDIO = "key_audio";
        public static final String KEY_VIDEO = "key_video";
        public static final String KEY_ARTICLE = "key_article";
        public static final String KEY_AUDIO_LIST = "key_audio_list";
        public static final String KEY_AUDIO_SELECTION = "key_audio_selection";
        public static final String KEY_PLAY_STATUS = "key_play_status";
        public static final String KEY_ID = "key_id";
        public static final String KEY_STAGE = "key_stage";
        public static final String KEY_TITLE = "key_title";
        public static final String KEY_FILTERED_LIST = "key_filtered_list";
        public static final String KEY_LIST = "key_list";
        public static final String KEY_IS_FILTERED = "key_is_filtered";

        public static final String KEY_PERSONALIZATION_LAUNCH = "key_personalization";

        public static final String INTENT_FILTER = "filter_intent";
        public static final String KEY_PDF_URL = "pdf_url";
        public static final String KEY_INCLUDE_CHILD_CONTENTS = "key_include_child_contents";
        public static final String KEY_IS_SUB_QUESTION = "key_is_sub_question";
    }

    public static class DATABASE {

        public static String TYPE_TEXT = " TEXT ";
        public static String TYPE_INTEGER = " INTEGER ";
        public static String COMMA = ", ";

        public static class DBINFO {
            public static Integer DB_VERSION = 1;
            public static String DATABASE_NAME = "nrna-database.db";
        }

        public static class TABLE_POST {

            public static String TABLE_NAME = "tbl_post";
            public static String COLUMN_ID = "id";
            public static String COLUMN_UPDATED_AT = "updated_date";
            public static String COLUMN_CREATED_AT = "created_date";
            public static String COLUMN_DATA = "data";
            public static String COLUMN_TYPE = "type";
            public static String COLUMN_STAGE = "stage";
            public static String COLUMN_LANGUAGE = "language";
            public static String COLUMN_TAGS = "tag";
            public static String COLUMN_SOURCE = "source";
            public static String COLUMN_DESCRIPTION = "description";
            public static String COLUMN_TITLE = "title";
            public static String COLUMN_DOWNLOAD_REF = "download_reference";
            public static String COLUMN_DOWNLOAD_STATUS = "download_status";

            public static String CREATE_TABLE_POST = "CREATE TABLE " + TABLE_NAME + " ( " +
                    COLUMN_ID + TYPE_INTEGER + " PRIMARY KEY" + COMMA +
                    COLUMN_UPDATED_AT + TYPE_INTEGER + COMMA +
                    COLUMN_CREATED_AT + TYPE_INTEGER + COMMA +
                    COLUMN_DATA + TYPE_TEXT + COMMA +
                    COLUMN_TYPE + TYPE_TEXT + COMMA +
                    COLUMN_STAGE + TYPE_TEXT + COMMA +
                    COLUMN_LANGUAGE + TYPE_TEXT + COMMA +
                    COLUMN_TAGS + TYPE_TEXT + COMMA +
                    COLUMN_SOURCE + TYPE_TEXT + COMMA +
                    COLUMN_DESCRIPTION + TYPE_TEXT + COMMA +
                    COLUMN_TITLE + TYPE_TEXT + COMMA +
                    COLUMN_DOWNLOAD_REF + TYPE_TEXT + COMMA +
                    COLUMN_DOWNLOAD_STATUS + TYPE_INTEGER +
                    " );";
        }

        public static class TABLE_QUESTION {

            public static String TABLE_NAME = "tbl_question";

            public static String COLUMN_ID = "id";
            public static String COlUMN_UPDATED_AT = "updated_date";
            public static String COLUMN_CREATED_AT = "created_date";
            public static String COLUMN_PARENT_ID = "parent_id";
            public static String COLUMN_TAGS = "tags";
            public static String COLUMN_LANGUAGE = "language";
            public static String COLUMN_TITLE = "title";
            public static String COLUMN_ANSWER = "answer";
            public static String COLUMN_STAGE = "stage";
            public static String COLUMN_CHILD_IDS = "child_ids";
            public static String COLUMN_CHILD_TITLES = "child_titles";
            public static String COLUMN_WEIGHT = "weight";

            public static String CREATE_TABLE_QUESTION = "Create table " + TABLE_NAME + "( " +
                    COLUMN_ID + TYPE_INTEGER + " primary key " + COMMA +
                    COlUMN_UPDATED_AT + TYPE_INTEGER + COMMA +
                    COLUMN_CREATED_AT + TYPE_INTEGER + COMMA +
                    COLUMN_PARENT_ID + TYPE_INTEGER + COMMA +
                    COLUMN_TAGS + TYPE_TEXT + COMMA +
                    COLUMN_LANGUAGE + TYPE_TEXT + COMMA +
                    COLUMN_STAGE + TYPE_TEXT + COMMA +
                    COLUMN_ANSWER + TYPE_TEXT + COMMA +
                    COLUMN_TITLE + TYPE_TEXT + COMMA +
                    COLUMN_WEIGHT + TYPE_INTEGER +
                    " );";

        }

        public static class TABLE_COUNTRY {

            public static String TABLE_NAME = "tbl_country";

            public static String COLUMN_ID = "id";
            public static String COlUMN_UPDATED_AT = "updated_date";
            public static String COLUMN_CREATED_AT = "created_date";
            public static String COLUMN_IMAGE = "image_url";
            public static String COLUMN_NAME = "name";
            public static String COLUMN_CODE = "code";
            public static String COLUMN_DESCRIPTION = "description";
            public static String COLUMN_CONTACTS = "contacts";
            public static String COLUMN_DOS_DONTS = "dos_donts";

            public static String CREATE_TABLE_COUNTRY = "Create table " + TABLE_NAME + "( " +
                    COLUMN_ID + TYPE_INTEGER + " primary key " + COMMA +
                    COlUMN_UPDATED_AT + TYPE_INTEGER + COMMA +
                    COLUMN_CREATED_AT + TYPE_INTEGER + COMMA +
                    COLUMN_IMAGE + TYPE_TEXT + COMMA +
                    COLUMN_CODE + TYPE_TEXT + COMMA +
                    COLUMN_NAME + TYPE_TEXT + COMMA +
                    COLUMN_DOS_DONTS + TYPE_TEXT + COMMA +
                    COLUMN_CONTACTS + TYPE_TEXT + COMMA +
                    COLUMN_DESCRIPTION + TYPE_TEXT + " );";

        }

        public static class TABLE_POST_QUESTION {

            public static String TABLE_NAME = "tbl_post_question";
            public static String COLUMN_ID = "id";
            public static String COLUMN_POST_ID = "post_id";
            public static String COLUMN_QUESTION_ID = "question_id";

            public static String CREATE_TABLE_POST_QUESTION = "Create table " + TABLE_NAME + "( " +
                    COLUMN_ID + TYPE_INTEGER + " primary key AUTOINCREMENT" + COMMA +
                    COLUMN_POST_ID + TYPE_INTEGER + COMMA +
                    COLUMN_QUESTION_ID + TYPE_INTEGER +
                    " );";
        }

        public static class TABLE_ANSWER {

            public static String TABLE_NAME = "tbl_answer";
            public static String COLUMN_ID = "id";
            public static String COLUMN_UPDATED_AT = "updated_date";
            public static String COLUMN_CREATED_AT = "created_date";
            public static String COLUMN_TITLE = "title";
            public static String COLUMN_QUESTION_ID = "question_id";

            public static String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ( " +
                    COLUMN_ID + TYPE_INTEGER + " PRIMARY KEY" + COMMA +
                    COLUMN_UPDATED_AT + TYPE_INTEGER + COMMA +
                    COLUMN_CREATED_AT + TYPE_INTEGER + COMMA +
                    COLUMN_TITLE + TYPE_TEXT + COMMA +
                    COLUMN_QUESTION_ID + TYPE_INTEGER +
                    " );";
        }

        public static class TABLE_POST_ANSWER {

            public static String TABLE_NAME = "tbl_post_answer";
            public static String COLUMN_ID = "id";
            public static String COLUMN_POST_ID = "post_id";
            public static String COLUMN_ANSWER_ID = "answer_id";

            public static String CREATE_TABLE = "Create table " + TABLE_NAME + "( " +
                    COLUMN_ID + TYPE_INTEGER + " primary key AUTOINCREMENT" + COMMA +
                    COLUMN_POST_ID + TYPE_INTEGER + COMMA +
                    COLUMN_ANSWER_ID + TYPE_INTEGER +
                    " );";
        }

        public static class TABLE_POST_COUNTRY {

            public static String TABLE_NAME = "tbl_post_country";
            public static String COLUMN_ID = "id";
            public static String COLUMN_POST_ID = "post_id";
            public static String COLUMN_COUNTRY_ID = "country_id";

            public static String CREATE_TABLE_POST_COUNTRY = "Create table " + TABLE_NAME + "( " +
                    COLUMN_ID + TYPE_INTEGER + " primary key AUTOINCREMENT" + COMMA +
                    COLUMN_POST_ID + TYPE_INTEGER + COMMA +
                    COLUMN_COUNTRY_ID + TYPE_INTEGER +
                    " );";
        }

        public static class TABLE_TAGS {
            public static String TABLE_NAME = "tbl_tags";
            public static String COLUUMN_ID = "id";
            public static String COLUMN_TAG = "tag";

            public static String CREATE_TABLE_TAGS = "Create table " + TABLE_NAME + "( " +
                    COLUUMN_ID + TYPE_INTEGER + " primary key AUTOINCREMENT " + COMMA +
                    COLUMN_TAG + TYPE_TEXT + " UNIQUE " +
                    " );";
        }

        public static class TABLE_COUNTRY_UPDATE {

            public static String TABLE_NAME = "tbl_country_update";
            public static String COLUMN_ID = "id";
            public static String COLUMN_UPDATED_AT = "updated_date";
            public static String COLUMN_CREATED_AT = "created_date";
            public static String COLUMN_TITLE = "title";
            public static String COLUMN_DESCRIPTION = "description";
            public static String COLUMN_COUNTRY_ID = "country_id";

            public static String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ( " +
                    COLUMN_ID + TYPE_INTEGER + " PRIMARY KEY" + COMMA +
                    COLUMN_UPDATED_AT + TYPE_INTEGER + COMMA +
                    COLUMN_CREATED_AT + TYPE_INTEGER + COMMA +
                    COLUMN_TITLE + TYPE_TEXT + COMMA +
                    COLUMN_DESCRIPTION + TYPE_TEXT + COMMA +
                    COLUMN_COUNTRY_ID + TYPE_INTEGER +
                    " );";
        }
    }

    public static class SECTION{
        public static final String JOURNEY = "journey";
    }
}
