package com.afra55.commontutils;

import android.content.Context;

/**
 * Created by Victor Yang on 2016/4/20.
 *
 */
public class AppCache {
    private static Context context;
    private static boolean isDebugMode;

    public static Context getContext() {
        return context;
    }

    public static void setContext(Context context) {
        AppCache.context = context;
    }

    public static boolean isDebugMode() {
        return isDebugMode;
    }

    public static void setIsDebugMode(boolean isDebugMode) {
        AppCache.isDebugMode = isDebugMode;
    }

    public static String getToken() {
        return "";
    }
}
