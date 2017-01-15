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
import static com.taf.util.MyConstants.Preferences.USER_INFO_SYNC_STATUS;
import static com.taf.util.MyConstants.Preferences.USER_ONBOARDING_COMPLETE;

public class AppPreferences {

    public static final String TAG = "AppPreferences";

    private SharedPreferences pref;
    private Context mContext;

    public AppPreferences(Context pContext) {
        pref = pContext.getSharedPreferences(PREF_NAME, Context
                .MODE_PRIVATE);
        mContext = pContext;
    }

    public Long getLastUpdateStamp() {
        return pref.getLong(LAST_UPDATE_STAMP, -1);
    }

    public void setLastUpdateStamp(long pStamp) {
        pref.edit().putLong(LAST_UPDATE_STAMP, pStamp).commit();
    }

    public boolean isUserInfoSynced() {
        return pref.getBoolean(USER_INFO_SYNC_STATUS, false);
    }

    public void setUserInfoSyncStatus(boolean status) {
        pref.edit().putBoolean(USER_INFO_SYNC_STATUS, status).commit();
    }

    public boolean isUserOnBoardingComplete() {
        return pref.getBoolean(USER_ONBOARDING_COMPLETE, false);
    }

    public void setUserOnBoardingComplete(boolean status) {
        pref.edit().putBoolean(USER_ONBOARDING_COMPLETE, status).commit();
    }

    public Long getLastDeleteStamp() {
        return pref.getLong(LAST_DELETE_STAMP, -1);
    }

    public void setLastDeleteStamp(long pStamp) {
        pref.edit().putLong(LAST_DELETE_STAMP, pStamp).commit();
    }

    public Set<String> getDownloadReferences() {
        return pref.getStringSet(DOWNLOAD_REFERENCES, new HashSet<>());
    }

    public void addDownloadReference(Long pReference) {
        Set<String> references = getDownloadReferences();
        references.add(String.valueOf(pReference));
        pref.edit().putStringSet(DOWNLOAD_REFERENCES, references).commit();
    }

    public void removeDownloadReference(Long pReference) {
        Set<String> references = getDownloadReferences();
        references.remove(String.valueOf(pReference));
        pref.edit().putStringSet(DOWNLOAD_REFERENCES, references).commit();
    }

    public boolean hasDownloadReference(Long pReference) {
        Set<String> references = getDownloadReferences();
        return references.contains(String.valueOf(pReference));
    }

    public String getPreviousWorkStatus() {
        return pref.getString(MyConstants.Preferences.PREVIOUS_WORK_STATUS, null);
    }

    public void setPreviousWorkStatus(String workStatus) {
        pref.edit().putString(MyConstants.Preferences.PREVIOUS_WORK_STATUS, workStatus).commit();
    }

    public String getGender() {
        return pref.getString(MyConstants.Preferences.GENDER, null);
    }

    public void setGender(String gender) {
        pref.edit().putString(MyConstants.Preferences.GENDER, gender).commit();
    }

    public boolean getFirstLaunch() {
        return pref.getBoolean(MyConstants.Preferences.FIRST_LAUNCH, true);
    }

    public void setFirstLaunch(boolean isFirstLaunch) {
        pref.edit().putBoolean(MyConstants.Preferences.FIRST_LAUNCH, isFirstLaunch).commit();
    }

    public String getLocation() {
        return pref.getString(MyConstants.Preferences.LOCATION,
                MyConstants.Preferences.DEFAULT_LOCATION);
    }

    public void setLocation(String location) {
        pref.edit().putString(MyConstants.Preferences.LOCATION, location).commit();
    }

    public String getUserName() {
        return pref.getString(MyConstants.Preferences.USERNAME, null);
    }

    public void setUserName(String userName) {
        pref.edit().putString(MyConstants.Preferences.USERNAME, userName).commit();
    }

    public Long getBirthday() {
        return pref.getLong(MyConstants.Preferences.BIRTHDAY, Long.MIN_VALUE);
    }

    public void setBirthday(Long birthday) {
        pref.edit().putLong(MyConstants.Preferences.BIRTHDAY, birthday).commit();
    }

    public int getOriginalLocation() {
        return pref.getInt(MyConstants.Preferences.ORIGINAL_LOCATION, Integer.MIN_VALUE);
    }

    // saves position of the zones in string array zones
    public void setOriginalLocation(int location) {
        pref.edit().putInt(MyConstants.Preferences.ORIGINAL_LOCATION, location).commit();
    }

    public boolean isOnBoardingCountryListLoaded() {
        return pref.getBoolean(MyConstants.Preferences.COUNTRY_LIST_CALL, false);
    }

    public void updateCountryListCallStatus(boolean status) {
        pref.edit().putBoolean(MyConstants.Preferences.COUNTRY_LIST_CALL, status).commit();
    }

    private void saveFavourites(Set<String> favourites) {
        pref.edit().remove(FAV_POSTS).commit();
        pref.edit().putStringSet(FAV_POSTS, favourites).commit();
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
        pref.edit().putLong(NOTICE_DISMISS_ID, id).commit();
    }
}
