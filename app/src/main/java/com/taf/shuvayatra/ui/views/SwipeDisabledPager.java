package com.taf.shuvayatra.ui.views;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by rakeeb on 10/26/16.
 */

public class SwipeDisabledPager extends ViewPager {

    public SwipeDisabledPager(Context context) {
        this(context, null);
    }

    public SwipeDisabledPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }
}
