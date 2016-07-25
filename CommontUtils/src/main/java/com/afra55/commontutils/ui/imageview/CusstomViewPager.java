package com.afra55.commontutils.ui.imageview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by Victor Yang on 2016/7/25.
 */
public class CusstomViewPager extends android.support.v4.view.ViewPager {

    public CusstomViewPager(Context context) {
        super(context);
    }

    public CusstomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        try {
            return super.onTouchEvent(ev);
        } catch (IllegalArgumentException ex) {
            Log.e("MultiTouchZoom", ev.toString());
        }
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException ex) {
            Log.e("MultiTouchZoom", ev.toString());
        }
        return false;
    }
}