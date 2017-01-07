package com.afra55.baseclient.base;

import android.app.Application;
import android.content.Context;
import android.os.StrictMode;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.afra55.baseclient.BuildConfig;
import com.afra55.baseclient.ui.activity.MainActivity;
import com.afra55.commontutils.AppCache;
import com.afra55.commontutils.crash.AppCrashHandler;
import com.afra55.commontutils.log.LogUtils;
import com.afra55.commontutils.storage.StorageType;
import com.afra55.commontutils.storage.StorageUtil;
import com.afra55.commontutils.sys.ScreenUtil;
import com.facebook.drawee.backends.pipeline.Fresco;


/**
 * Created by yangshuai in the 10:37 of 2016.01.05 .
 */
public class CustomApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Fresco.initialize(this.getApplicationContext());
        AppCache.setContext(this.getApplicationContext());

        // init tools
        StorageUtil.init(this, null);
        ScreenUtil.init(this);

        // init log
        String path = StorageUtil.getDirectoryByDirType(StorageType.TYPE_LOG);
        LogUtils.init(path, Log.DEBUG);

        /* 异常捕获(debug 时不捕获异常) */
        if (!BuildConfig.DEBUG) {
            AppCrashHandler.getInstance(this, MainActivity.class);
        } else {
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
        }

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
