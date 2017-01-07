package com.afra55.baseclient.view;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.afra55.baseclient.R;
import com.afra55.baseclient.util.AlertUtils;
import com.afra55.commontutils.log.LogUtils;
import com.afra55.commontutils.ui.dialog.EasyEditDialog;

/**
 * Created by yangshuai in the 14:06 of 2016.01.06 .
 */
public class CustomWebChromeClient extends WebChromeClient {

    private Context context;

    public CustomWebChromeClient(Context context) {
        this.context = context;
    }

    /* 处理Javascript中的Alert对话框 */
    @Override
    public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
        if (!message.isEmpty()) {
            AlertUtils.showAlert(
                    context
                    , "Tip"
                    , message
                    , "Ok"
                    , "Cancel"
                    , new AlertUtils.OnDialogClickListener() {
                @Override
                public void onConfirm(DialogInterface dialog, int which) {
                    result.confirm();
                }

                @Override
                public void onCancel(DialogInterface dialog, int which) {
                    result.cancel();
                }

            });
        }
        return super.onJsAlert(view, url, message, result);
    }

    @Override
    public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
        if (!message.isEmpty()) {
            AlertUtils.showAlert(
                    context
                    , "Tip"
                    , message
                    , "Ok"
                    , "Cancel"
                    , new AlertUtils.OnDialogClickListener() {
                        @Override
                        public void onConfirm(DialogInterface dialog, int which) {
                            result.confirm();
                        }

                        @Override
                        public void onCancel(DialogInterface dialog, int which) {
                            result.cancel();
                        }
                    });
        }
        return super.onJsConfirm(view, url, message, result);
    }

    @Override
    public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
        LogUtils.d("CustomWebChromeClient", "defaultValue = " + defaultValue + " result = " + result + " message = " + message);
        promptAlert(context, "Tip", message, result);
        return super.onJsPrompt(view, url, message, defaultValue, result);
    }

    private void promptAlert(Context context, String title, String message, final JsPromptResult jsPromptResult) {
        final EasyEditDialog requestDialog = new EasyEditDialog(context);
        requestDialog.setEditTextMaxLength(32);
        requestDialog.setEditText(message);
        requestDialog.setTitle(title);
        requestDialog.addNegativeButtonListener(R.string.cancel, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestDialog.dismiss();
                jsPromptResult.cancel();
            }
        });
        requestDialog.addPositiveButtonListener(R.string.ok, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestDialog.dismiss();
                String msg = requestDialog.getEditMessage();
                jsPromptResult.confirm(msg);
            }
        });
        requestDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                jsPromptResult.cancel();
            }
        });
        requestDialog.show();
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        super.onProgressChanged(view, newProgress);
    }
}
