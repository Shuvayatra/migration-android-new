package com.taf.shuvayatra.util;


import android.content.Context;
import android.content.SharedPreferences;

import static com.taf.util.MyConstants.Preferences.*;

/**
 * Created by julian on 12/16/15.
 */
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

    public long getLastUpdateStamp() {
        return pref.getLong(LAST_UPDATE_STAMP, -1);
    }

    public void setLastUpdateStamp(long pStamp) {
        editor.putLong(LAST_UPDATE_STAMP, pStamp);
        editor.apply();
    }
}
