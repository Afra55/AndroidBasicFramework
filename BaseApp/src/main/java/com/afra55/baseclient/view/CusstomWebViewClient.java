package com.afra55.baseclient.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.afra55.commontutils.base.BaseActivity;
import com.afra55.commontutils.log.LogUtil;

/**
 * Created by yangshuai in the 13:58 of 2016.01.06 .
 */
public class CusstomWebViewClient extends WebViewClient {

    private final Context context;

    public CusstomWebViewClient(Context context) {
        this.context = context;
    }

    /* url 拦截, 在shouldoverrideurlloading中只对要拦截处理的url返回true，否则一定要返回false ，这样就能避免重定向问题。 */
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        return super.shouldOverrideUrlLoading(view, request);
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        LogUtil.i("webview", "start load");
        view.getSettings().setBlockNetworkImage(true);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        view.getSettings().setBlockNetworkImage(false);
        super.onPageFinished(view, url);
        LogUtil.i("webview", "finish load");
        BaseActivity baseActivity = (BaseActivity) context;
        if (baseActivity != null && !baseActivity.isFinishing()) {
            view.getSettings().setBlockNetworkImage(false);
        }
    }

    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        super.onReceivedError(view, request, error);
        view.stopLoading();
    }

    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        handler.proceed();
    }
}
