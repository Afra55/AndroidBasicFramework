package com.afra55.commontutils.sys;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

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

    private static String channel = null;

    /**
     * 获取 META-INF 文件下的渠道号， 这个渠道号是一个空文件的名字，前缀是 channel_xxxxx , xxxxx就是渠道号。
     * 这个空文件需要手动放进去。
     * 未测试。
     * @param context
     * @return
     */
    public static String getChannel(Context context) {
        if (channel != null) {
            return channel;
        }

        final String start_flag = "META-INF/channel_";
        ApplicationInfo appinfo = context.getApplicationInfo();
        String sourceDir = appinfo.sourceDir;
        ZipFile zipfile = null;
        try {
            zipfile = new ZipFile(sourceDir);
            Enumeration<?> entries = zipfile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = ((ZipEntry) entries.nextElement());
                String entryName = entry.getName();
                if (entryName.contains(start_flag)) {
                    channel = entryName.replace(start_flag, "");
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (zipfile != null) {
                try {
                    zipfile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        if (channel == null || channel.length() <= 0) {
            channel = "default-developer";//读不到渠道号就默认官方渠道
        }
        return channel;
    }
}
