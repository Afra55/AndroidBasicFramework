package com.afra55.commontutils.plugin;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;

import com.afra55.commontutils.R;
import com.afra55.commontutils.bean.PluginInfoBean;
import com.afra55.commontutils.storage.StorageUtil;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

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
            pluginInfoBean.setPluginName(pm.getApplicationLabel(appInfo).toString());//app名称
            pluginInfoBean.setPluginPackageName(appInfo.packageName);//包名
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

    /**
     * 加载apk获得内部资源 ID, 然后使用 插件的 Resources 获取资源
     *
     * @param context context
     * @param pluginInfoBean  PluginInfoBean
     * @throws Exception
     */
    private int dynamicLoadApk(Context context, PluginInfoBean pluginInfoBean, String resourceName) throws Exception {
        File dexOutputDir = context.getDir("dex", 0);
        if (dexOutputDir == null && !dexOutputDir.mkdir()) {
            throw new Resources.NotFoundException("dex dir not found");
        }
        //参数：1、包含dex的apk文件或jar文件的路径，2、apk、jar解压缩生成dex存储的目录，3、本地library库目录，一般为null，4、父ClassLoader
        DexClassLoader dexClassLoader = new DexClassLoader(
                StorageUtil.getPluginDir() + pluginInfoBean.getPluginName()
                , dexOutputDir.getPath()
                , null
                , ClassLoader.getSystemClassLoader());

        //通过使用apk自己的类加载器，反射出R类中相应的内部类进而获取我们需要的资源id
        Class<?> clazz = dexClassLoader.loadClass(pluginInfoBean.getPluginPackageName() + ".R$mipmap");

        //得到名为 resourceName 字段
        Field field = clazz.getDeclaredField(resourceName);
        return field.getInt(R.id.class);
    }
}
