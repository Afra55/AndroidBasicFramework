package com.afra55.commontutils.bean;

import android.graphics.drawable.Drawable;

import java.io.Serializable;

/**
 * Created by Victor Yang on 2016/10/8.
 * 插件信息 bean
 */

public class PluginInfoBean implements Serializable{

    //版本号
    private String versionName;
    private Drawable icon;

    //插件名称
    String appName;
    String packageName;

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
}
