package com.taf.data.entity;

import android.support.annotation.StringDef;

import com.taf.util.MyConstants;
import com.taf.util.MyConstants.DynamicScreen;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 *
 */
@Retention(RetentionPolicy.SOURCE)
@StringDef({DynamicScreen.TYPE_FEED, DynamicScreen.TYPE_BLOCK})
public @interface ScreenType {
}
