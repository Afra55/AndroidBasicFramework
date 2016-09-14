package com.afra55.commontutils.sys;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

/**
 * Created by Victor Yang on 2016/8/19.
 *
 */
public class ConfigInfoUtil {

    /**
     * 获取清单文件中 meta_data 标签的 value
     * @param context context
     * @param key key
     * @return value
     */
    public static Object getManifestsMetaDataValue(Context context, String key) {
        try {
            ApplicationInfo appInfo = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            if (appInfo != null) {
                return appInfo.metaData.get(key);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
