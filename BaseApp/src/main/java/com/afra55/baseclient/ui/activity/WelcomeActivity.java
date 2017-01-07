package com.afra55.baseclient.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.afra55.baseclient.R;
import com.afra55.baseclient.base.presenter.WelcomeActivityPresenter;
import com.afra55.baseclient.base.ui.WelcomeActivityUI;
import com.afra55.commontutils.base.BaseActivity;
import com.afra55.commontutils.log.LogUtils;

public class WelcomeActivity extends BaseActivity implements WelcomeActivityUI{

    private static final String TAG = "WelcomeActivity";

    private WelcomeActivityPresenter mWelcomeActivityPresenter;

    private boolean customSplash = false;

    private static boolean firstEnter = true; // 是否首次进入

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        mWelcomeActivityPresenter = new WelcomeActivityPresenter(this);

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
                    mWelcomeActivityPresenter.handleFirstEnter();
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
    @Override
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

    @Override
    public void showMainActivity() {
        showMainActivity(null);
    }

    @Override
    public void showMainActivity(Intent intent) {
        LogUtils.i(TAG, "show main activity");
        if (isDestroyedCompatible()) {
            return;
        }
        MainActivity.start(WelcomeActivity.this, intent);
        finish();
    }
}
