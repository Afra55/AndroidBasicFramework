package com.afra55.commontutils.device;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.ResultReceiver;
import android.util.Log;

/**
 * Created by Afra55 on 2017/12/4.
 * Smile is the best name card.
 */

public class KeyboardReceiver extends ResultReceiver {

    private Handler handler;

    /**
     * Create a new ResultReceive to receive results.  Your
     * {@link #onReceiveResult} method will be called from the thread running
     * <var>handler</var> if given, or from an arbitrary thread if null.
     *
     * @param handler
     */
    public KeyboardReceiver(Handler handler) {
        super(handler);
        this.handler = handler;
    }

    /**
     * @param resultCode {@link android.view.inputmethod.InputMethodManager#RESULT_HIDDEN}
     * @param resultData what is it?
     */
    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        super.onReceiveResult(resultCode, resultData);
        Log.i("Fuck", resultCode + " " + (resultData != null ? resultData.toString() : ""));
        if (handler != null) {
            Message msg = new Message();
            msg.what = resultCode;
            msg.setData(resultData);
            handler.sendMessage(msg);
        }

    }
}
