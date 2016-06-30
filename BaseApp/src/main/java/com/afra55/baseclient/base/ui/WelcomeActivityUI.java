package com.afra55.baseclient.base.ui;

import android.content.Intent;

/**
 * Created by Victor Yang on 2016/6/30.
 */
public interface WelcomeActivityUI {

    void showMainActivity();

    void showMainActivity(Intent intent);

    void onIntent();

    boolean isDestroyedCompatible();
}
