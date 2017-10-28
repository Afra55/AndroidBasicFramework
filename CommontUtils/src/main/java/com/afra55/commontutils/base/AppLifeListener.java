package com.afra55.commontutils.base;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.afra55.commontutils.log.LogUtils;

/**
 * Created by Afra55 on 2017/10/28.
 * Smile is the best name card.
 * 应用生命周期管理
 */

public class AppLifeListener implements Application.ActivityLifecycleCallbacks {

    private static final String TAG = LogUtils.makeLogTag(AppLifeListener.class);

    private int foregroundActivities;//后台activity
    private boolean hasSeenFirstActivity;
    private boolean isChangingConfiguration;

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
    }

    /**
     * 在应用界面显示过程中
     */
    @Override
    public void onActivityStarted(Activity activity) {
        foregroundActivities++;
        if (hasSeenFirstActivity && foregroundActivities == 1 && !isChangingConfiguration) {
            // 应用启动
        }
        hasSeenFirstActivity = true;
        isChangingConfiguration = false;
        LogUtils.i(TAG,
                "onActivityStarted: foregroundActivities=" + foregroundActivities
                        + " | activity:" + activity.toString());
    }

    @Override
    public void onActivityResumed(Activity activity) {
        LogUtils.i(TAG, "onActivityResumed: foregroundActivities=" + foregroundActivities
                + " | activity:" + activity.toString());
    }

    @Override
    public void onActivityPaused(Activity activity) {

        LogUtils.i(TAG, "onActivityPaused: foregroundActivities=" + foregroundActivities
                + " | activity:" + activity.toString());
    }

    /**
     * 在应用界面不显示的过程中
     */
    @Override
    public void onActivityStopped(Activity activity) {
        foregroundActivities--;
        if (foregroundActivities == 0) {
            // 应用进入后台
        }
        isChangingConfiguration = activity.isChangingConfigurations();
        LogUtils.i(TAG, "onActivityStopped: foregroundActivities=" + foregroundActivities
                + " | activity:" + activity.toString());
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        LogUtils.i(TAG, "onActivitySaveInstanceState: foregroundActivities=" + foregroundActivities
                + " | activity:" + activity.toString());
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        LogUtils.i(TAG, "onActivityDestroyed: foregroundActivities=" + foregroundActivities
                + " | activity:" + activity.toString());
    }


}
