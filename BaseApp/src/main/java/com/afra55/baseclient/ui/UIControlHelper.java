package com.afra55.baseclient.ui;

import android.content.Context;
import android.content.Intent;

import com.afra55.baseclient.ui.activity.MainActivity;
import com.afra55.baseclient.ui.activity.WelcomeActivity;

/**
 * Created by Victor Yang on 2017/1/3 0003.
 * UIControlHelper, 作为中间人，管理所有 activity 的启动.
 * 也作为字典，快捷查找各个功能模块对应的 Activity。
 */

public class UIControlHelper {

    private UIControlHelper() {
    }

    public static void startWelcomeActivity(Context context) {
        WelcomeActivity.start(context);
    }

    public static void startMainActivity(Context context, Intent extras) {
        MainActivity.start(context, extras);
    }

}
