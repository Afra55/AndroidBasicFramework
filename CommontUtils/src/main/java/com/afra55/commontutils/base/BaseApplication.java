package com.afra55.commontutils.base;

import android.app.Application;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.res.Configuration;
import android.os.Looper;
import android.support.multidex.MultiDex;
import android.text.TextUtils;
import android.util.Log;

import com.afra55.commontutils.AppCache;
import com.afra55.commontutils.format.TimeUtils;
import com.afra55.commontutils.log.LogUtils;
import com.afra55.commontutils.storage.StorageType;
import com.afra55.commontutils.storage.StorageUtil;
import com.afra55.commontutils.sys.ScreenUtil;
import com.afra55.commontutils.sys.SystemUtil;
import com.afra55.commontutils.tip.ToastUtils;
import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by Afra55 on 2017/10/28.
 * Smile is the best name card.
 */

public abstract class BaseApplication extends Application {

    private static final String TAG = LogUtils.makeLogTag(BaseApplication.class);

    private Context instance;
    private AppLifeListener appLifeListener;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        String processName = SystemUtil.getProcessName(this);
        if (TextUtils.isEmpty(processName)) {
            return;
        }
        PackageInfo packageInfo = SystemUtil.getPackageInfo(this);
        if (TextUtils.equals(packageInfo.packageName, processName)) {
            // 当前添加判断的主要原因在于，当应用存在多进程问题是会重复初始化执行多次。
            AppCache.setContext(this.getApplicationContext());
            appLifeListener = new AppLifeListener();
            registerActivityLifecycleCallbacks(appLifeListener);
            registerComponentCallbacks(new AppMemoryCall());
            TimeUtils.setAppStartTime(getApplicationContext(), System.currentTimeMillis());

            Fresco.initialize(this.getApplicationContext());


            // init tools
            StorageUtil.init(this, null);
            ScreenUtil.init(this);

            // init log
            String path = StorageUtil.getDirectoryByDirType(StorageType.TYPE_LOG);
            LogUtils.init(path, Log.DEBUG);

            initConfig();

        }
    }

    protected abstract void initConfig();

    public AppLifeListener getAppLifeListener() {
        return appLifeListener;
    }

    public class AppMemoryCall implements ComponentCallbacks2 {

        @Override
        public void onConfigurationChanged(Configuration configuration) {

        }

        @Override
        public void onLowMemory() {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                ToastUtils.showToast(instance, "当前内存不足");
            }
        }

        @Override
        public void onTrimMemory(int level) {
            switch (level) {
                case TRIM_MEMORY_UI_HIDDEN:
                    // 进行资源释放操作
                    LogUtils.d(TAG, "当前的ui为不可见的");
                    break;
                case TRIM_MEMORY_RUNNING_MODERATE:
                    LogUtils.d(TAG, "表示应用程序正常运行，并且不会被杀掉。但是目前手机的内存已经有点低了，系统可能会开始根据LRU缓存规则来去杀死进程了。\n");
                    break;
                case TRIM_MEMORY_RUNNING_LOW:
                    LogUtils.d(TAG, "表示应用程序正常运行，并且不会被杀掉。但是目前手机的内存已经非常低了，我们应该去释放掉一些不必要的资源以提升系统的性能，同时这也会直接影响到我们应用程序的性能。\n");
                    break;
                case TRIM_MEMORY_RUNNING_CRITICAL:
                    LogUtils.d(TAG, " 表示应用程序仍然正常运行，但是系统已经根据LRU缓存规则杀掉了大部分缓存的进程了。这个时候我们应当尽可能地去释放任何不必要的资源，不然的话系统可能会继续杀掉所有缓存中的进程，并且开始杀掉一些本来应当保持运行的进程，比如说后台运行的服务。\n" +
                            "     以上是当我们的应用程序正在运行时的回调，那么如果我们的程序目前是被缓存的，则会收到以下几种类型的回调：\n");
                    break;
                case TRIM_MEMORY_BACKGROUND:
                    LogUtils.d(TAG, "表示手机目前内存已经很低了，系统准备开始根据LRU缓存来清理进程。这个时候我们的程序在LRU缓存列表的最近位置，是不太可能被清理掉的，但这时去释放掉一些比较容易恢复的资源能够让手机的内存变得比较充足，从而让我们的程序更长时间地保留在缓存当中，这样当用户返回我们的程序时会感觉非常顺畅，而不是经历了一次重新启动的过程。\n");
                    break;
                case TRIM_MEMORY_MODERATE:
                    LogUtils.d(TAG, "表示手机目前内存已经很低了，并且我们的程序处于LRU缓存列表的中间位置，如果手机内存还得不到进一步释放的话，那么我们的程序就有被系统杀掉的风险了。\n");
                    break;
                case TRIM_MEMORY_COMPLETE:
                    ToastUtils.showToast(instance, "当前运行内存过低");
                    LogUtils.d(TAG, "表示手机目前内存已经很低了，并且我们的程序处于LRU缓存列表的最边缘位置，系统会最优先考虑杀掉我们的应用程序，在这个时候应当尽可能地把一切可以释放的东西都进行释放。\n");
                    break;
            }

            if (level >= ComponentCallbacks2.TRIM_MEMORY_MODERATE) {
                // Clear the caches.  Note all pending requests will be removed too.

            }
        }
    }
}
