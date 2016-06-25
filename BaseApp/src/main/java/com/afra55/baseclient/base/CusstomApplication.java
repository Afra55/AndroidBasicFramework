package com.afra55.baseclient.base;

import android.app.Application;
import android.os.StrictMode;
import android.util.Log;

import com.afra55.baseclient.BuildConfig;
import com.afra55.commontutils.AppCache;
import com.afra55.commontutils.crash.AppCrashHandler;
import com.afra55.commontutils.log.LogUtil;
import com.afra55.commontutils.storage.StorageType;
import com.afra55.commontutils.storage.StorageUtil;
import com.afra55.commontutils.sys.ScreenUtil;


/**
 * Created by yangshuai in the 10:37 of 2016.01.05 .
 */
public class CusstomApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        AppCache.setContext(this.getApplicationContext());

        // init tools
        StorageUtil.init(this, null);
        ScreenUtil.init(this);

        // init log
        String path = StorageUtil.getDirectoryByDirType(StorageType.TYPE_LOG);
        LogUtil.init(path, Log.DEBUG);

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
}
