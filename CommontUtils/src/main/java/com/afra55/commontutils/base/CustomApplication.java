package com.afra55.commontutils.base;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.afra55.commontutils.AppCache;
import com.afra55.commontutils.format.TimeUtils;
import com.afra55.commontutils.log.LogUtils;
import com.afra55.commontutils.storage.StorageType;
import com.afra55.commontutils.storage.StorageUtil;
import com.afra55.commontutils.sys.ScreenUtil;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.raizlabs.android.dbflow.config.FlowManager;


/**
 * Created by yangshuai in the 10:37 of 2016.01.05 .
 */
public class CustomApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        TimeUtils.setAppStartTime(getApplicationContext(), System.currentTimeMillis());

        // 数据库初始化 https://yumenokanata.gitbooks.io/dbflow-tutorials/content/index.html
        FlowManager.init(this);



        Fresco.initialize(this.getApplicationContext());
        AppCache.setContext(this.getApplicationContext());

        // init tools
        StorageUtil.init(this, null);
        ScreenUtil.init(this);

        // init log
        String path = StorageUtil.getDirectoryByDirType(StorageType.TYPE_LOG);
        LogUtils.init(path, Log.DEBUG);

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
