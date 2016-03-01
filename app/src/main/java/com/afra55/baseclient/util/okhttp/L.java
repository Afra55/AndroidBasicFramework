package com.afra55.baseclient.util.okhttp;

import android.util.Log;

public class L
{
    private static boolean debug = false;

    public static void e(String msg)
    {
        if (debug)
        {
            Log.e("OkHttp", msg);
        }
    }

}

