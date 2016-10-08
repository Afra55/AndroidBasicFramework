package com.afra55.commontutils.plugin;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;

import com.afra55.commontutils.bean.PluginInfoBean;
import com.afra55.commontutils.storage.StorageUtil;

import java.lang.reflect.Method;

/**
 * Created by Victor Yang on 2016/10/8.
 * 插件化开发
 */

public class PluginUtils {

    /**
     * 获取未安装apk的信息
     *
     * @param context         context
     * @param archiveFilePath apk文件的path
     * @return {}
     */
    private PluginInfoBean getUninstallApkInfo(Context context, String archiveFilePath) {
        PackageManager pm = context.getPackageManager();
        PackageInfo pkgInfo = pm.getPackageArchiveInfo(archiveFilePath, PackageManager.GET_ACTIVITIES);
        PluginInfoBean pluginInfoBean = new PluginInfoBean();
        if (pkgInfo != null) {
            ApplicationInfo appInfo = pkgInfo.applicationInfo;
            pluginInfoBean.setVersionName(pkgInfo.versionName);//版本号
            pluginInfoBean.setIcon(pm.getApplicationIcon(appInfo));//图标
            pluginInfoBean.setAppName(pm.getApplicationLabel(appInfo).toString());//app名称
            pluginInfoBean.setPackageName(appInfo.packageName);//包名
        }
        return pluginInfoBean;
    }

    /**
     * @param apkName
     * @return 得到对应插件的Resource对象
     */
    private Resources getPluginResources(Context context, String apkName) {
        try {
            AssetManager assetManager = AssetManager.class.newInstance();

            //反射调用方法addAssetPath(String path)
            Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);

            // 第二个参数是apk的路径：Environment.getExternalStorageDirectory().getPath()+File.separator+"plugin"+File.separator+"apkplugin.apk"
            // 将未安装的Apk文件的添加进AssetManager中，第二个参数为apk文件的路径带apk名
            addAssetPath.invoke(assetManager, StorageUtil.getPluginDir() + apkName);
            Resources superRes = context.getResources();
            return new Resources(assetManager, superRes.getDisplayMetrics(),
                    superRes.getConfiguration());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
