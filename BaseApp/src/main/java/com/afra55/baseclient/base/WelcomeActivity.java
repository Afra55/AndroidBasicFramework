package com.afra55.baseclient.base;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.afra55.baseclient.R;
import com.afra55.commontutils.base.BaseActivity;
import com.afra55.commontutils.log.LogUtil;

public class WelcomeActivity extends BaseActivity {

    private static final String TAG = "WelcomeActivity";

    private boolean customSplash = false;

    private static boolean firstEnter = true; // 是否首次进入

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
    protected void onResume() {
        super.onResume();

        if (firstEnter) {
            firstEnter = false;
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    if (isDestroyedCompatible()) {
                        return;
                    }
                    if (canAutoLogin()) {
                        onIntent();
                    } else {
                        // 到登陆界面,
                        showMainActivity();
                        finish();
                    }
                }
            };
            if (customSplash) {
                new Handler().postDelayed(runnable, 1000);
            } else {
                runnable.run();
            }
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
    private void onIntent() {
        LogUtil.i(TAG, "onIntent...");
        showMainActivity();
    }

    /**
     * 已经登陆过，自动登陆
     */
    private boolean canAutoLogin() {
        // 处理
        return false;
    }

    /**
     * 首次进入，打开欢迎界面
     */
    private void showSplashView() {
        getWindow().setBackgroundDrawableResource(R.drawable.splash_bg);
        customSplash = true;
    }

    private void showMainActivity() {
        showMainActivity(null);
    }

    private void showMainActivity(Intent intent) {
        LogUtil.i(TAG, "show main activity");
        if (isDestroyedCompatible()) {
            return;
        }
        MainActivity.start(WelcomeActivity.this, intent);
        finish();
    }
}
