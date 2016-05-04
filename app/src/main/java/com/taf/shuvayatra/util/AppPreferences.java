package com.taf.shuvayatra.util;


import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

import static com.taf.util.MyConstants.Preferences.DOWNLOAD_REFERENCES;
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
}
