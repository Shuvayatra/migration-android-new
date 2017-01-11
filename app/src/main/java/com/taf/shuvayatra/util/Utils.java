package com.taf.shuvayatra.util;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import com.taf.util.MyConstants;

import java.util.Locale;

import static java.security.AccessController.getContext;

/**
 * Created by julian on 12/17/15.
 */
public class Utils {
    public static ColorStateList getIconColorTint(int color, int checkedColor) {
        return new ColorStateList(
                new int[][]{
                        new int[]{android.R.attr.state_checked},
                        new int[]{}
                },
                new int[]{
                        checkedColor,
                        color
                }
        );
    }

    public static void setLanguage(int i, Context context) {
        switch (i) {
            case MyConstants.Language.ENGLISH:
                Locale localeEN = new Locale("en");
                Locale.setDefault(localeEN);
                Configuration configEn = new Configuration();
                configEn.locale = localeEN;
                context.getApplicationContext().getResources().updateConfiguration(configEn, null);
                break;
            case MyConstants.Language.NEPALI:
                Locale localeNp = new Locale("np");
                Locale.setDefault(localeNp);
                Configuration configNp = new Configuration();
                configNp.locale = localeNp;
                context.getApplicationContext().getResources().updateConfiguration(configNp, null);
                break;
        }
    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }
}
