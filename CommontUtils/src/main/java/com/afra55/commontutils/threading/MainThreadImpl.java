package com.afra55.commontutils.threading;

import android.os.Handler;
import android.os.Looper;

import com.afra55.commontutils.base.MainThread;


/**
 * This class makes sure that the runnable we provide will be run on the main UI thread.
 * <p/>
 * Created by dmilicic on 7/29/15.
 */
public class MainThreadImpl implements MainThread {

    private static MainThread sMainThread;

    private Handler mHandler;

    private MainThreadImpl() {
        mHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void post(Runnable runnable) {
        mHandler.post(runnable);
    }

    public static MainThread getInstance() {
        if (sMainThread == null) {
            sMainThread = new MainThreadImpl();
        }

        return sMainThread;
    }
}
