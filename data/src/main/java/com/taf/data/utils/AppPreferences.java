package com.taf.data.utils;


import android.content.Context;
import android.content.SharedPreferences;

import com.taf.util.MyConstants;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static com.taf.util.MyConstants.Preferences.DOWNLOAD_REFERENCES;
import static com.taf.util.MyConstants.Preferences.FAV_POSTS;
import static com.taf.util.MyConstants.Preferences.LAST_DELETE_STAMP;
import static com.taf.util.MyConstants.Preferences.LAST_UPDATE_STAMP;
import static com.taf.util.MyConstants.Preferences.NOTICE_DISMISS_ID;
import static com.taf.util.MyConstants.Preferences.PREF_NAME;

public class AppPreferences {

    public static final String TAG = "AppPreferences";

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context mContext;

    public AppPreferences(Context pContext) {
        pref = pContext.getSharedPreferences(PREF_NAME, Context
                .MODE_PRIVATE);
        editor = pref.edit();
        mContext = pContext;
    }

    public Long getLastUpdateStamp() {
        return pref.getLong(LAST_UPDATE_STAMP, -1);
    }

    public void setLastUpdateStamp(long pStamp) {
        editor.putLong(LAST_UPDATE_STAMP, pStamp);
        editor.apply();
    }

    public Long getLastDeleteStamp() {
        return pref.getLong(LAST_DELETE_STAMP, -1);
    }

    public void setLastDeleteStamp(long pStamp) {
        editor.putLong(LAST_DELETE_STAMP, pStamp);
        editor.apply();
    }

    public Set<String> getDownloadReferences() {
        return pref.getStringSet(DOWNLOAD_REFERENCES, new HashSet<>());
    }

    public void addDownloadReference(Long pReference) {
        Set<String> references = getDownloadReferences();
        references.add(String.valueOf(pReference));
        editor.putStringSet(DOWNLOAD_REFERENCES, references);
        editor.commit();
    }

    public void removeDownloadReference(Long pReference) {
        Set<String> references = getDownloadReferences();
        references.remove(String.valueOf(pReference));
        editor.putStringSet(DOWNLOAD_REFERENCES, references);
        editor.commit();
    }

    public boolean hasDownloadReference(Long pReference) {
        Set<String> references = getDownloadReferences();
        return references.contains(String.valueOf(pReference));
    }

    public int getPreviousWorkStatus() {
        return pref.getInt(MyConstants.Preferences.PREVIOUS_WORK_STATUS, Integer.MIN_VALUE);
    }

    public void setPreviousWorkStatus(int workStatus) {
        editor.putInt(MyConstants.Preferences.PREVIOUS_WORK_STATUS, workStatus);
        editor.apply();
    }

    public String getGender() {
        return pref.getString(MyConstants.Preferences.GENDER, null);
    }

    public void setGender(String gender) {
        editor.putString(MyConstants.Preferences.GENDER, gender);
        editor.apply();
    }

    public boolean getFirstLaunch() {
        return pref.getBoolean(MyConstants.Preferences.FIRST_LAUNCH, true);
    }

    public void setFirstLaunch(boolean isFirstLaunch) {
        editor.putBoolean(MyConstants.Preferences.FIRST_LAUNCH, isFirstLaunch);
        editor.apply();
    }

    public String getLocation() {
        return pref.getString(MyConstants.Preferences.LOCATION, MyConstants.Preferences.DEFAULT_LOCATION);
    }

    public void setLocation(String location) {
        editor.putString(MyConstants.Preferences.LOCATION, location);
        editor.apply();
    }

    public String getUserName() {
        return pref.getString(MyConstants.Preferences.USERNAME, null);
    }

    public void setUserName(String userName) {
        editor.putString(MyConstants.Preferences.USERNAME, userName);
        editor.apply();
    }

    public Long getBirthday() {
        return pref.getLong(MyConstants.Preferences.BIRTHDAY, Long.MIN_VALUE);
    }

    public void setBirthday(Long birthday) {
        editor.putLong(MyConstants.Preferences.BIRTHDAY, birthday);
        editor.apply();
    }

    public int getOriginalLocation() {
        return pref.getInt(MyConstants.Preferences.ORIGINAL_LOCATION, Integer.MIN_VALUE);
    }

    // saves position of the zones in string array zones
    public void setOriginalLocation(int location) {
        editor.putInt(MyConstants.Preferences.ORIGINAL_LOCATION, location);
        editor.apply();
    }

    public boolean isOnBoardingCountryListLoaded() {
        return pref.getBoolean(MyConstants.Preferences.COUNTRY_LIST_CALL, false);
    }

    public void updateCountryListCallStatus(boolean status) {
        editor.putBoolean(MyConstants.Preferences.COUNTRY_LIST_CALL, status);
        editor.apply();
    }

    private void saveFavourites(Set<String> favourites) {
        editor.remove(FAV_POSTS);
        editor.apply();
        editor.putStringSet(FAV_POSTS, favourites);
        editor.apply();
    }

    public void addToFavourites(Long postId) {
        if (postId != null) {
            Set<String> favourites = pref.getStringSet(FAV_POSTS, new HashSet<>());
            favourites.add(String.valueOf(postId));
            saveFavourites(favourites);
        }
    }

    public void removeFromFavourites(Long postId) {
        if (postId != null) {
            Set<String> favourites = pref.getStringSet(FAV_POSTS, new HashSet<>());
            favourites.remove(String.valueOf(postId));
            saveFavourites(favourites);
        }
    }

    public boolean isFavourite(Long postId) {
        if (postId != null) {
            Set<String> favourites = pref.getStringSet(FAV_POSTS, new HashSet<>());
            return favourites.contains(String.valueOf(postId));
        }
        return false;
    }

    public Long getNoticeDismissId() {
        return pref.getLong(NOTICE_DISMISS_ID, -1);
    }

    public void setNoticeDismissId(Long id) {
        editor.putLong(NOTICE_DISMISS_ID, id);
        editor.apply();
    }
}
