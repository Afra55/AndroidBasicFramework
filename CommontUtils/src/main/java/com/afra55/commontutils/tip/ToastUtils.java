package com.afra55.commontutils.tip;

import android.widget.Toast;

import com.afra55.commontutils.base.BaseActivity;
import com.afra55.commontutils.log.LogUtil;

/**
 * Created by Victor Yang on 2016/6/28.
 */
public class ToastUtils {

    private static final String TAG = ToastUtils.class.getSimpleName();

    //Toast公共方法
    private static Toast toast = null;

    public static void showToast(BaseActivity context, String message) {
        if (context.isDestroyedCompatible()) {
            return;
        }
        if (toast == null) {
            toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        } else {
            toast.setText(message);
        }
        toast.show();
    }

    public static <T> T showToast(BaseActivity context, T what){
        try {
            showToast(context, String.valueOf(what));
        } catch (Exception e) {
            LogUtil.e(TAG, e.toString());
        }
        return what;
    }

    public static void showToast(BaseActivity context, String message, int showLength) {
        if (context.isDestroyedCompatible()) {
            return;
        }
        if (toast == null) {
            toast = Toast.makeText(context, message, showLength);
        } else {
            toast.setDuration(showLength);
            toast.setText(message);
        }
        toast.show();
    }
}
