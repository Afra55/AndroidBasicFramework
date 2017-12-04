package com.afra55.commontutils.device;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by Victor Yang on 2016/6/25.
 */
public class KeyBoardUtils {
    public static void showKeyboard(Activity activity, boolean isShow) {
        if (activity == null) {
            return;
        }

        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) {
            return;
        }

        if (isShow) {
            if (activity.getCurrentFocus() == null) {
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            } else {
                imm.showSoftInput(activity.getCurrentFocus(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
            }
        } else {
            if (activity.getCurrentFocus() != null) {
                imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }

        }
    }

    public static boolean showKeyboard(Activity activity, boolean isShow, KeyboardReceiver resultReceiver) {
        if (activity == null) {
            return false;
        }

        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) {
            return false;
        }

        if (isShow) {
            if (activity.getCurrentFocus() == null) {
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                return false;
            } else {
                return imm.showSoftInput(activity.getCurrentFocus(), InputMethodManager.RESULT_UNCHANGED_SHOWN, resultReceiver);
            }
        } else {
            if (activity.getCurrentFocus() != null) {
                // 可以通过返回值 来判断键盘是否显示，false 即键盘隐藏状态，这个时候 receiver 也不会接收到信息
                return imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS, resultReceiver);
            }

        }
        return false;
    }
    public static void hideKeyboard(Activity activity, View view) {
        if (activity == null) {
            return;
        }

        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) {
            return;
        }

        imm.hideSoftInputFromWindow(
                view.getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
