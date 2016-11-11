package com.taf.interactor;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class UseCaseData implements Serializable {
    public static final String LIMIT = "limit";
    public static final String OFFSET = "offset";
    public static final String FAVOURITE_STATE = "is_favourite";
    public static final String SYNC_LIST = "sync_list";
    public static final String SEARCH_UN_SYNCED_DATA = "searchUnSyncedData";
    public static final String LAST_UPDATED = "last_updated";
    public static final String DOWNLOAD_REFERENCE = "download_reference";
    public static final String DOWNLOAD_STATUS = "download_status";
    public static final String EXCLUDE_LIST = "exclude_list";
    public static final String CACHED_DATA = "cached_data";
    public static final String SUBMISSION_DATA = "submission_data";

    public static final String IS_SEARCH = "is_seearch";
    public static final String SEARCH_QUERY = "search_query";
    public static final String SEARCH_TYPE = "search_type";
    public static final String CATEGORY_ID = "category_id";
    public static final String POST_TYPE = "type";
    public static final String IS_FAVOURITE = "is_favourite";

    public static final String COMPONENT_TYPE = "component_type";

    Map<String, Object> data = null;

    public UseCaseData() {
        data = new HashMap<>();
    }

    public void clearAll() {
        data.clear();
    }

    public void putSerializable(String key, Serializable value) {
        data.put(key, value);
    }

    public Serializable getSerializable(String key) {
        final Object o = data.get(key);
        try {
            return (Serializable) o;
        } catch (ClassCastException e) {
            return null;
        }
    }

    public void putString(String key, String value) {
        data.put(key, value);
    }

    public String getString(String key) {
        final Object o = data.get(key);
        try {
            return (String) o;
        } catch (ClassCastException e) {
            return null;
        }
    }

    public String getString(String key, String defValue) {
        final String s = getString(key);
        return (s == null) ? defValue : s;
    }

    public void putInteger(String key, Integer value) {
        data.put(key, value);
    }

    public Integer getInteger(String key, Integer defValue) {
        final Integer i = getInteger(key);
        return (i == null) ? defValue : i;
    }

    public Integer getInteger(String key) {
        final Object o = data.get(key);
        try {
            return (Integer) o;
        } catch (ClassCastException e) {
            return null;
        }
    }

    public void putBoolean(String key, Boolean value) {
        data.put(key, value);
    }

    public Boolean getBoolean(String key, Boolean defValue) {
        final Boolean i = getBoolean(key);
        return (i == null) ? defValue : i;
    }

    public Boolean getBoolean(String key) {
        final Object o = data.get(key);
        try {
            return (Boolean) o;
        } catch (ClassCastException e) {
            return null;
        }
    }

    public void putDouble(String key, Double value) {
        data.put(key, value);
    }

    public Double getDouble(String key, Double defValue) {
        final Double i = getDouble(key);
        return (i == null) ? defValue : i;
    }

    public Double getDouble(String key) {
        final Object o = data.get(key);
        try {
            return (Double) o;
        } catch (ClassCastException e) {
            return null;
        }
    }

    public void putLong(String key, Long value) {
        data.put(key, value);
    }

    public Long getLong(String key, Long defValue) {
        final Long i = getLong(key);
        return (i == null) ? defValue : i;
    }

    public Long getLong(String key) {
        final Object o = data.get(key);
        try {
            return (Long) o;
        } catch (ClassCastException e) {
            return null;
        }
    }

    @Override
    public String toString() {
        String value = "";
        for (String key : data.keySet()) {
            value += key + " : " + data.get(key) + "\n";
        }
        return value;
    }
}
