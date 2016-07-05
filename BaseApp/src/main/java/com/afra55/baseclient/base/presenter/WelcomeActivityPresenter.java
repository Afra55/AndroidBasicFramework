package com.afra55.baseclient.base.presenter;

import com.afra55.baseclient.base.ui.WelcomeActivityUI;
import com.example.shuai.apimodule.helper.LoginUtils;

/**
 * Created by Victor Yang on 2016/6/30.
 */
public class WelcomeActivityPresenter {

    private WelcomeActivityUI mWelcomeActivityUI;

    public WelcomeActivityPresenter(WelcomeActivityUI welcomeActivityUI) {
        this.mWelcomeActivityUI = welcomeActivityUI;
    }

    public void handleFirstEnter() {
        if (mWelcomeActivityUI.isDestroyedCompatible()) {
            return;
        }
        if (LoginUtils.getInstance().canAutoLogin()) {
            mWelcomeActivityUI.onIntent();
        } else {
            // 到登陆界面, 本工程没有登陆界面，需自行更换
            mWelcomeActivityUI.showMainActivity();
        }
    }
}
