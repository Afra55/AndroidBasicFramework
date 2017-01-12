package com.afra55.baseclient.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.afra55.apimodule.helper.LoginUtils;
import com.afra55.baseclient.R;
import com.afra55.commontutils.base.BaseActivity;
import com.afra55.commontutils.log.LogUtils;

public class WelcomeActivity extends BaseActivity{

    private static final String TAG = "WelcomeActivity";

    private boolean customSplash = false;

    private static boolean firstEnter = true; // 是否首次进入

    public static void start(Context context) {
        Intent intent = new Intent(context, WelcomeActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        if (savedInstanceState != null) {
            setIntent(new Intent()); // 从堆栈恢复，不再重复解析之前的intent
        }
        if (!firstEnter) {
            onIntent();
        } else {
            showSplashView();
        }
    }

    @Override
    protected String getScreenTitle() {
        return null;
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (firstEnter) {
            firstEnter = false;
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    handleFirstEnter();
                }
            };
            if (customSplash) {
                new Handler().postDelayed(runnable, 1000);
            } else {
                runnable.run();
            }
        }
    }

    public void handleFirstEnter() {
        if (isDestroyedCompatible()) {
            return;
        }
        if (LoginUtils.getInstance().canAutoLogin()) {
            onIntent();
        } else {
            // 到登陆界面, 本工程没有登陆界面，需自行更换
            showMainActivity();
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.clear();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        /**
         * 如果Activity在，不会走到onCreate，而是onNewIntent，这时候需要setIntent
         * 场景：点击通知栏跳转到此，会收到Intent
         */
        setIntent(intent);
        onIntent();
    }

    // 处理收到的Intent
    public void onIntent() {
        LogUtils.i(TAG, "onIntent...");
        showMainActivity();
    }

    /**
     * 首次进入，打开欢迎界面
     */
    private void showSplashView() {
        getWindow().setBackgroundDrawableResource(R.drawable.splash_bg);
        customSplash = true;
    }

    public void showMainActivity() {
        showMainActivity(null);
    }

    public void showMainActivity(Intent intent) {
        LogUtils.i(TAG, "show main activity");
        if (isDestroyedCompatible()) {
            return;
        }
        MainActivity.start(WelcomeActivity.this, intent);
        finish();
    }
}
