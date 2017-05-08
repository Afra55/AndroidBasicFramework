package com.afra55.commontutils.plugin;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Bundle;

import com.afra55.commontutils.R;
import com.afra55.commontutils.bean.PluginInfoBean;
import com.afra55.commontutils.file.AttachmentStore;
import com.afra55.commontutils.log.LogUtils;
import com.afra55.commontutils.storage.StorageUtil;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import dalvik.system.DexClassLoader;

/**
 * Created by Victor Yang on 2016/10/8.
 * 插件化开发
 */

public class PluginUtils {

    private static final String TAG = PluginUtils.class.getSimpleName();

    private static Map<String, AssetManager> mPluginAssetManagerCache = new HashMap<>();
    private static Map<String, Resources> mPluginResourcesCache = new HashMap<>();
    private static Map<String, Resources.Theme> mPluginThemeCache = new HashMap<>();

    /**
     * 清楚插件缓存
     */
    public static void clearPluginCache() {
        mPluginAssetManagerCache.clear();
        mPluginResourcesCache.clear();
        mPluginThemeCache.clear();
    }

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
            if ((pkgInfo.activities != null)
                    && (pkgInfo.activities.length > 0)) {
                pluginInfoBean.setPluginMainClassName(pkgInfo.activities[0].name);
            }
        }
        return pluginInfoBean;
    }

    /**
     * 获取插件的 AssetManager.
     * @param context Context.
     * @param dexName 插件的名字.
     * @return 插件的 AssetManager.
     */
    public static AssetManager getPluginAssetManager(Context context, String dexName) {
        if (mPluginAssetManagerCache.containsKey(dexName) && mPluginAssetManagerCache.get(dexName) != null) {
            return mPluginAssetManagerCache.get(dexName);
        }
        try {
            AssetManager assetManager = AssetManager.class.newInstance();

            //反射调用方法addAssetPath(String path)
            Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);

            // 第二个参数是apk的路径：
            // Environment.getExternalStorageDirectory().getPath()+File.separator+"plugin"+File.separator+"apkplugin.apk"
            // 将未安装的Apk文件的添加进AssetManager中，第二个参数为apk文件的路径带apk名
            addAssetPath.invoke(assetManager, StorageUtil.getPluginDir() + dexName);

            mPluginAssetManagerCache.put(dexName, assetManager);
            return assetManager;
        } catch (Exception e) {
            return context.getAssets();
        }
    }

    /**
     * 获取插件的 Resources
     * @param context Context
     * @param dexName 插件名字
     * @return 插件的 Resources
     */
    public static Resources getPluginResources(Context context, String dexName) {
        if (mPluginResourcesCache.containsKey(dexName) && mPluginResourcesCache.get(dexName) != null) {
            return mPluginResourcesCache.get(dexName);
        }
        try {
            AssetManager assetManager = getPluginAssetManager(context, dexName);
            Resources superRes = context.getResources();
            Resources resources = new Resources(assetManager, superRes.getDisplayMetrics(),
                    superRes.getConfiguration());
            mPluginResourcesCache.put(dexName, resources);
            return resources;
        } catch (Exception e) {
            return context.getResources();
        }
    }

    /**
     * 获取插件的主题
     * @param context Context
     * @param apkName 插件名字
     * @return 插件的主题
     */
    public static Resources.Theme getPluginTheme(Context context, String apkName) {
        if (mPluginThemeCache.containsKey(apkName) && mPluginThemeCache.get(apkName) != null) {
            return mPluginThemeCache.get(apkName);
        }
        try {
            Resources resources = getPluginResources(context, apkName);
            Resources.Theme mTheme = resources.newTheme();
            mTheme.setTo(context.getTheme());
            mPluginThemeCache.put(apkName, mTheme);
            return mTheme;
        } catch (Exception e) {
            return context.getTheme();
        }
    }

    /**
     * 加载apk获得内部资源 ID, 然后使用 插件的 Resources 获取资源
     *
     * @param context        context
     * @param pluginInfoBean PluginInfoBean
     * @throws Exception
     */
    private int dynamicLoadApk(Context context, PluginInfoBean pluginInfoBean, String resourceName) throws Exception {

        DexClassLoader dexClassLoader =
                ClassLoaderHelper.getClassLoader(
                        context
                        , StorageUtil.getPluginDir() + pluginInfoBean.getPluginName()
                        , ClassLoader.getSystemClassLoader());

        //通过使用apk自己的类加载器，反射出R类中相应的内部类进而获取我们需要的资源id
        Class<?> clazz = dexClassLoader.loadClass(pluginInfoBean.getPluginPackageName() + ".R$mipmap");

        //得到名为 resourceName 字段
        Field field = clazz.getDeclaredField(resourceName);
        return field.getInt(R.id.class);
    }

    /**
     * 启动 插件的 Activity.
     * @param context Context
     * @param pluginName 插件名字
     * @param className 需要启动的 Activity 名字（例: com.afra55.baseclient.base.MainActivity）
     * @param bundle Bundle
     * @param needAgencyTargetActivity 是否代理目标 Activity ，
     *                                 如果代理则需要实现 setProxy(Activity proxyActivity) 方法，
     *                                 并代理全部的生命周期及其他。
     */
    public static void launchTargetActivity(
            Context context
            , String pluginName
            , final String className
            , Bundle bundle
            , boolean needAgencyTargetActivity) {
        LogUtils.d(TAG, "start launchTargetActivity, className=" + className);

        ClassLoader localClassLoader = ClassLoader.getSystemClassLoader();
        DexClassLoader dexClassLoader =
                ClassLoaderHelper.getClassLoader(
                        context
                        , StorageUtil.getPluginDir() + pluginName
                        , localClassLoader);
        try {
            Class<?> localClass = dexClassLoader.loadClass(className);
            Constructor<?> localConstructor = localClass
                    .getConstructor(new Class[]{});
            Object instance = localConstructor.newInstance(new Object[]{});
            LogUtils.d(TAG, "instance = " + instance);

            if (needAgencyTargetActivity) {
                // 需要实现 setProxy(Activity proxyActivity) 方法
                // 如果实现这个方法，则由传入的 context 代理打开的 activity。
                Method setProxy = localClass.getMethod("setProxy",
                        new Class[]{Activity.class});
                setProxy.setAccessible(true);
                setProxy.invoke(instance, new Object[]{context});
            }

            Method onCreate = localClass.getDeclaredMethod("onCreate",
                    new Class[]{Bundle.class});
            onCreate.setAccessible(true);

            // bundle 会由 onCreate(Bundle savedInstanceState)中的 savedInstanceState 接收
            if (bundle == null) {
                bundle = new Bundle();
            }
            onCreate.invoke(instance, new Object[]{bundle});
        } catch (Exception e) {
            LogUtils.e(TAG, "launchTargetActivity", e);
        }
    }

    /**
     * 加载 SD 卡上的 .so 库文件
     * @param context Context
     * @param soLibraryPath .so 库文件 路径
     * @param soLibraryName .so 库文件 名字
     */
    public static void loadSdcardSOLibrary(Context context, String soLibraryPath, String soLibraryName) {
        File dir = context.getDir("jniLibs", Activity.MODE_PRIVATE);
        File distFile = new File(dir.getAbsolutePath() + File.separator + soLibraryName);
        if (AttachmentStore.copy(soLibraryPath, distFile.getAbsolutePath()) != -1) {
            System.loadLibrary(distFile.getAbsolutePath());
        }
    }
}
