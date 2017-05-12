package com.afra55.commontutils.log;

import android.util.Log;

public class LogUtils {

    private static String LOG_PREFIX = "afra55_";
    private static int LOG_PREFIX_LENGTH = LOG_PREFIX.length();
    private static final int MAX_LOG_TAG_LENGTH = 23;

    private static boolean LOGGING_ENABLED;

    public static void setLoggingEnabled(boolean loggingEnabled) {
        LOGGING_ENABLED = loggingEnabled;
    }

    public static String makeLogTag(String str) {
        if (str.length() > MAX_LOG_TAG_LENGTH - LOG_PREFIX_LENGTH) {
            return LOG_PREFIX + str.substring(0, MAX_LOG_TAG_LENGTH - LOG_PREFIX_LENGTH - 1);
        }

        return LOG_PREFIX + str;
    }

    /**
     * Don't use this when obfuscating class names!
     */
    public static String makeLogTag(Class cls) {
        return makeLogTag(cls.getSimpleName());
    }

    public static void init(String logFile, int level) {
        LogImpl.init(logFile, level);
    }

    public static void v(String tag, String msg) {
        if (LOGGING_ENABLED && Log.isLoggable(tag, Log.DEBUG)) {
            LogImpl.v(tag, buildMessage(msg));
        }
    }

    public static void v(String tag, String msg, Throwable thr) {
        if (LOGGING_ENABLED && Log.isLoggable(tag, Log.DEBUG))
            LogImpl.v(tag, buildMessage(msg), thr);
    }

    public static void d(String tag, String msg) {
        if (LOGGING_ENABLED && Log.isLoggable(tag, Log.DEBUG))
            LogImpl.d(tag, buildMessage(msg));
    }

    public static void d(String tag, String msg, Throwable thr) {
        if (LOGGING_ENABLED && Log.isLoggable(tag, Log.DEBUG))
            LogImpl.d(tag, buildMessage(msg), thr);
    }

    public static void i(String tag, String msg) {
        if (LOGGING_ENABLED && Log.isLoggable(tag, Log.DEBUG))
            LogImpl.i(tag, buildMessage(msg));
    }

    public static void i(String tag, String msg, Throwable thr) {
        if (LOGGING_ENABLED && Log.isLoggable(tag, Log.DEBUG))
            LogImpl.i(tag, buildMessage(msg), thr);
    }

    public static void w(String tag, String msg) {
        LogImpl.w(tag, buildMessage(msg));
    }

    public static void w(String tag, String msg, Throwable thr) {
        LogImpl.w(tag, buildMessage(msg), thr);
    }

    public static void w(String tag, Throwable thr) {
        LogImpl.w(tag, buildMessage(""), thr);
    }

    public static void e(String tag, String msg) {
        LogImpl.e(tag, buildMessage(msg));
    }

    public static void e(String tag, String msg, Throwable thr) {
        LogImpl.e(tag, buildMessage(msg), thr);
    }

    public static void ui(String msg) {
        String tag = "ui";
        if (LOGGING_ENABLED && Log.isLoggable(tag, Log.DEBUG))
            LogImpl.i(tag, buildMessage(msg));
    }

    public static void core(String msg) {
        String tag = "core";
        if (LOGGING_ENABLED && Log.isLoggable(tag, Log.DEBUG))
            LogImpl.i(tag, buildMessage(msg));
    }

    public static void coreDebug(String msg) {
        String tag = "core";
        if (LOGGING_ENABLED && Log.isLoggable(tag, Log.DEBUG))
            LogImpl.d(tag, buildMessage(msg));
    }

    public static void res(String msg) {
        String tag = "RES";
        if (LOGGING_ENABLED && Log.isLoggable(tag, Log.DEBUG))
            LogImpl.i(tag, buildMessage(msg));
    }

    public static void resDebug(String msg) {
        String tag = "RES";
        if (LOGGING_ENABLED && Log.isLoggable(tag, Log.DEBUG))
            LogImpl.d(tag, buildMessage(msg));
    }

    public static void audio(String msg) {
        String tag = "AudioRecorder";
        if (LOGGING_ENABLED && Log.isLoggable(tag, Log.DEBUG))
            LogImpl.i(tag, buildMessage(msg));
    }

    public static void vincent(String msg) {
        String tag = "Vincent";
        if (LOGGING_ENABLED && Log.isLoggable(tag, Log.DEBUG))
            LogImpl.v(tag, buildMessage(msg));
    }

    public static void pipeline(String prefix, String msg) {
        String tag = "Pipeline";
        if (LOGGING_ENABLED && Log.isLoggable(tag, Log.DEBUG))
            LogImpl.i(tag, prefix + " " + buildMessage(msg));
    }

    public static void pipelineDebug(String prefix, String msg) {
        String tag = "Pipeline";
        if (LOGGING_ENABLED && Log.isLoggable(tag, Log.DEBUG))
            LogImpl.d(tag, prefix + " " + buildMessage(msg));
    }

    public static String getLogFileName(String cat) {
        return LogImpl.getLogFileName(cat);
    }

    private static String buildMessage(String msg) {
        return msg;
    }
}
