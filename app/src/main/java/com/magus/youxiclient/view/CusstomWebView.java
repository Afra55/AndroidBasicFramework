package com.magus.youxiclient.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

/**
 * Created by yangshuai in the 13:51 of 2016.01.06 .
 */
public class CusstomWebView extends WebView {

    public CusstomWebView(Context context) {
        super(context);
        init(context);
    }

    public CusstomWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CusstomWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        getSettings().setDefaultTextEncodingName("utf-8"); // 设置默认的显示编码
        getSettings().setSupportZoom(false); // 允许缩放
        getSettings().setBuiltInZoomControls(false); // 不显示放大缩小 controler
        getSettings().setJavaScriptEnabled(true);
        getSettings().setDomStorageEnabled(true); // 语序WebView执行JS
        getSettings().setAppCacheMaxSize(1024 * 1024 * 8);// 设置缓冲大小，8M
        setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY); // 隐藏滚动条
        // getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE); // 不使用缓存，只用网络加载
        getSettings().setCacheMode(WebSettings.LOAD_DEFAULT); // 不使用缓存，只用网络加载
        getSettings().setBlockNetworkImage(true); // 先加载网页文字，后加载图片
        String appCacheDir = context.getApplicationContext()
                .getDir("cache", Context.MODE_PRIVATE).getPath();
        getSettings().setAppCachePath(appCacheDir);
        getSettings().setAllowFileAccess(true);// 可以读取文件缓存(manifest生效)
        getSettings().setAppCacheEnabled(true);
        getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH); // 提高渲染等级
        setHorizontalScrollBarEnabled(false);// 水平不显示
        setVerticalScrollBarEnabled(false); // 垂直不显示
        getSettings().setPluginState(WebSettings.PluginState.OFF);
        getSettings().setUseWideViewPort(true);
        getSettings().setLoadWithOverviewMode(true);// 设置网页能适配手机屏幕

        setWebChromeClient(new CusstomWebChromeClient(context));
        setWebViewClient(new CusstomWebViewClient(context));
    }
}
