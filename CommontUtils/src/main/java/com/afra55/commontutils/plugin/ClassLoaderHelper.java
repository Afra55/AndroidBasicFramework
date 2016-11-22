package com.afra55.commontutils.plugin;

import android.content.Context;

import java.io.File;
import java.util.HashMap;

import dalvik.system.DexClassLoader;

/**
 * Created by Victor Yang on 2016/10/11.
 * ClassLoaderHelper
 */
class ClassLoaderHelper extends DexClassLoader {

    private static final String TAG = "ClassLoaderHelper";

    private static final HashMap<String, ClassLoaderHelper> mPluginClassLoaders = new HashMap<>();

    //参数：1、包含dex的apk文件或jar文件的路径，2、apk、jar解压缩生成dex存储的目录，3、本地library库目录，一般为null，4、父ClassLoader
    private ClassLoaderHelper(String dexPath, String optimizedDirectory, String librarySearchPath, ClassLoader parent) {
        super(dexPath, optimizedDirectory, librarySearchPath, parent);
    }

    /**
     * return a available classloader which belongs to different apk
     */
    static ClassLoaderHelper getClassLoader(Context context, String dexPath, ClassLoader parentLoader) {
        ClassLoaderHelper dLClassLoader = mPluginClassLoaders.get(dexPath);
        if (dLClassLoader != null)
            return dLClassLoader;

        File dexOutputDir = context.getDir("dex", Context.MODE_PRIVATE);
        final String dexOutputPath = dexOutputDir.getAbsolutePath();
        dLClassLoader = new ClassLoaderHelper(dexPath, dexOutputPath, null, parentLoader);
        mPluginClassLoaders.put(dexPath, dLClassLoader);

        return dLClassLoader;
    }
}
