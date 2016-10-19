package com.taf.shuvayatra.util;


import android.content.Context;
import android.content.SharedPreferences;

import com.taf.util.MyConstants;

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

    public void setPreviousWorkStatus(boolean workStatus){
        editor.putBoolean(MyConstants.Preferences.PREVIOUS_WORK_STATUS, workStatus);
        editor.apply();
    }

    public boolean getPreviousWorkStatus(){
        return pref.getBoolean(MyConstants.Preferences.PREVIOUS_WORK_STATUS,false);
    }

    public void setGender(String gender){
        editor.putString(MyConstants.Preferences.GENDER, gender);
        editor.apply();
    }

    public String getGender(){
        return pref.getString(MyConstants.Preferences.GENDER,null);
    }

    public void setFirstLaunch(boolean isFirstLaunch){
        editor.putBoolean(MyConstants.Preferences.FIRST_LAUNCH, isFirstLaunch);
        editor.apply();
    }

    public boolean getFirstLaunch(){
        return pref.getBoolean(MyConstants.Preferences.FIRST_LAUNCH,true);
    }
}
