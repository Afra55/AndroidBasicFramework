package com.afra55.commontutils.tip;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Victor Yang on 2016/6/28.
 */
public class ToastUtils {
    //Toast公共方法
    private static Toast toast = null;

    public static void showToast(Context context, String message) {
        if (toast == null) {
            toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        } else {
            toast.setText(message);
        }
        toast.show();
    }

    public static void showToast(Context context, String message, int showLength) {
        if (toast == null) {
            toast = Toast.makeText(context, message, showLength);
        } else {
            toast.setDuration(showLength);
            toast.setText(message);
        }
        toast.show();
    }
}
