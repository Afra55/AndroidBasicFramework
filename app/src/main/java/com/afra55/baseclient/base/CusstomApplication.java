package com.afra55.baseclient.base;

import android.app.Application;

/**
 * Created by yangshuai in the 10:37 of 2016.01.05 .
 */
public class CusstomApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        /* 异常捕获(debug 时不捕获异常) */
        Thread.setDefaultUncaughtExceptionHandler(new MyUncaughtExceptionHandler(this));
    }
}
