package com.afra55.commontutils;

import android.content.Context;

/**
 * Created by Victor Yang on 2016/4/20.
 */
public class AppCache {
    private static Context context;
    private static boolean isDebug;
    private static String appServiceUrl;

    public static String getAppServiceUrl() {
        return appServiceUrl;
    }

    public static void setAppServiceUrl(String appServiceUrl) {
        AppCache.appServiceUrl = appServiceUrl;
    }

    public static Context getContext() {
        return context;
    }

    public static void setContext(Context context) {
        AppCache.context = context;
    }

    public static boolean isDebug() {
        return isDebug;
    }

    public static void setIsDebug(boolean isDebug) {
        AppCache.isDebug = isDebug;
    }

    public static String getToken() {
        return "";
    }


}
