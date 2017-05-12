package com.afra55.baseclient.common;

import android.os.StrictMode;

import com.afra55.baseclient.BuildConfig;
import com.afra55.baseclient.ui.activity.MainActivity;
import com.afra55.commontutils.AppCache;
import com.afra55.commontutils.base.CustomApplication;
import com.afra55.commontutils.crash.AppCrashHandler;

/**
 * Created by yangshuai on 2017/5/12.
 */

public class AppApplication extends CustomApplication {

    @Override
    public void onCreate() {
        super.onCreate();
         /* 异常捕获(debug 时不捕获异常) */
        AppCrashHandler.getInstance(this, MainActivity.class);
        if (BuildConfig.DEBUG) {
            AppCache.setIsDebug(true);
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectDiskReads()
                    .detectDiskWrites()
                    .detectAll()   // or .detectAll() for all detectable problems
                    .penaltyLog()
                    .penaltyDialog()
                    .build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects()
                    .detectLeakedClosableObjects()
                    .penaltyLog()
                    .penaltyDeath()
                    .build());
        } else {
            AppCache.setIsDebug(false);
        }
    }
}