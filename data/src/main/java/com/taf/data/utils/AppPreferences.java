package com.taf.data.utils;


import android.content.Context;
import android.content.SharedPreferences;

import com.taf.util.MyConstants;
import com.taf.util.MyConstants.OnBoarding;

import java.util.HashSet;
import java.util.Set;

import static com.taf.util.MyConstants.Preferences.DOWNLOAD_REFERENCES;
import static com.taf.util.MyConstants.Preferences.LAST_DELETE_STAMP;
import static com.taf.util.MyConstants.Preferences.LAST_UPDATE_STAMP;
import static com.taf.util.MyConstants.Preferences.PREF_NAME;

public class AppPreferences {
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
        return pref.getStringSet(DOWNLOAD_REFERENCES, new HashSet<String>());
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

    public void setPreviousWorkStatus(int workStatus) {
        editor.putInt(MyConstants.Preferences.PREVIOUS_WORK_STATUS, workStatus);
        editor.apply();
    }

    public int getPreviousWorkStatus() {
        return pref.getInt(MyConstants.Preferences.PREVIOUS_WORK_STATUS, Integer.MIN_VALUE);
    }

    public void setGender(String gender) {
        editor.putString(MyConstants.Preferences.GENDER, gender);
        editor.apply();
    }

    public String getGender() {
        return pref.getString(MyConstants.Preferences.GENDER, null);
    }

    public void setFirstLaunch(boolean isFirstLaunch) {
        editor.putBoolean(MyConstants.Preferences.FIRST_LAUNCH, isFirstLaunch);
        editor.apply();
    }

    public boolean getFirstLaunch() {
        return pref.getBoolean(MyConstants.Preferences.FIRST_LAUNCH, true);
    }

    public void setLocation(String location) {
        editor.putString(MyConstants.Preferences.LOCATION, location);
        editor.apply();
    }

    public String getLocation() {
        return pref.getString(MyConstants.Preferences.LOCATION, MyConstants.Preferences.DEFAULT_LOCATION);
    }

    public void setUserName(String userName) {
        editor.putString(MyConstants.Preferences.USERNAME, userName);
        editor.apply();
    }

    public String getUserName() {
        return pref.getString(MyConstants.Preferences.USERNAME, null);
    }

    public void setBirthday(Long birthday) {
        editor.putLong(MyConstants.Preferences.BIRTHDAY, birthday);
        editor.apply();
    }

    public Long getBirthday() {
        return pref.getLong(MyConstants.Preferences.BIRTHDAY, Long.MIN_VALUE);
    }

    // saves position of the zones in string array zones
    public void setOriginalLocation(int location) {
        editor.putInt(MyConstants.Preferences.ORIGINAL_LOCATION, location);
        editor.apply();
    }

    public int getOriginalLocation() {
        return pref.getInt(MyConstants.Preferences.ORIGINAL_LOCATION, Integer.MIN_VALUE);
    }

    public boolean isOnBoardingCountryListLoaded() {
        return pref.getBoolean(MyConstants.Preferences.COUNTRY_LIST_CALL, false);
    }

    public void updateCountryListCallStatus(boolean status) {
        editor.putBoolean(MyConstants.Preferences.COUNTRY_LIST_CALL, status);
        editor.apply();
    }

    /**
     * Only used while {@link OnBoarding} as all of the fragments present in the activity might not
     * have been loaded.
     *
     * @return returns country list as [id],[name]
     */
    public Set<String> getCountryList() {
        return pref.getStringSet(MyConstants.Preferences.COUNTRY_LIST, new HashSet<>());
    }

    public void updateCountryList(Set<String> countrySet) {
        editor.putStringSet(MyConstants.Preferences.COUNTRY_LIST, countrySet);
        editor.apply();
    }

}
