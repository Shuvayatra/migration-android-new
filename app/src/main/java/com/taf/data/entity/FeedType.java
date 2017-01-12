package com.taf.data.entity;

import android.support.annotation.IntDef;

import com.taf.shuvayatra.base.FeedFragment;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 *
 */
@Retention(RetentionPolicy.SOURCE)
@IntDef({FeedFragment.FEED_GENERAL, FeedFragment.FEED_NEWS})
public @interface FeedType {
}
