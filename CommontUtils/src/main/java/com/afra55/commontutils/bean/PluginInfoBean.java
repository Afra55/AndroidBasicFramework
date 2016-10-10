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
    private String pluginName;
    private String pluginPackageName;

    // 插件的主程序
    private String pluginMainClassName;

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

    public String getPluginName() {
        return pluginName;
    }

    public void setPluginName(String pluginName) {
        this.pluginName = pluginName;
    }

    public String getPluginPackageName() {
        return pluginPackageName;
    }

    public void setPluginPackageName(String pluginPackageName) {
        this.pluginPackageName = pluginPackageName;
    }

    public String getPluginMainClassName() {
        return pluginMainClassName;
    }

    public void setPluginMainClassName(String pluginMainClassName) {
        this.pluginMainClassName = pluginMainClassName;
    }
}
