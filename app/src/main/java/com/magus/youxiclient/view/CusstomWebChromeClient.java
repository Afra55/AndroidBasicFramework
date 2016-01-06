package com.magus.youxiclient.view;

import android.content.Context;
import android.content.DialogInterface;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.magus.youxiclient.util.AlertUtils;
import com.magus.youxiclient.util.Log;

/**
 * Created by yangshuai in the 14:06 of 2016.01.06 .
 */
public class CusstomWebChromeClient extends WebChromeClient {

    private Context context;

    public CusstomWebChromeClient(Context context) {
        this.context = context;
    }

    /* 处理Javascript中的Alert对话框 */
    @Override
    public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
        if (!message.isEmpty()) {
            AlertUtils.showConfirmAlert(
                    context, "提示", message
                    , new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            result.cancel();
                        }
                    });
        }
        return false;
    }

    @Override
    public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
        if (!message.isEmpty()) {
            AlertUtils.showConfirmAlert(
                    context, "提示", message
                    , new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            result.cancel();
                        }
                    });
        }
        return true;
    }

    @Override
    public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
        Log.d("CusstomWebChromeClient", "defaultValue = " + defaultValue + " result = " + result + " message = " + message);
        return super.onJsPrompt(view, url, message, defaultValue, result);
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        super.onProgressChanged(view, newProgress);
    }
}
