package com.afra55.commontutils.log;

import android.util.Log;

import com.afra55.commontutils.BuildConfig;

import javax.annotation.Nonnull;

public class LogUtils {

    private static String LOG_PREFIX = "afra55_";
    private static int LOG_PREFIX_LENGTH = LOG_PREFIX.length();
    private static final int MAX_LOG_TAG_LENGTH = 23;

    public static boolean LOGGING_ENABLED = !BuildConfig.BUILD_TYPE.equalsIgnoreCase("release");

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

    private static String getSimpleClassName(String name) {
        int lastIndex = name.lastIndexOf(".");
        return name.substring(lastIndex + 1);
    }

    private static int getStackOffset(StackTraceElement[] trace) {
        for (int i = 2; i < trace.length; i++) {
            StackTraceElement e = trace[i];
            String name = e.getClassName();
            if (!name.equals(LogUtils.class.getName())) {
                return --i;
            }
        }
        return -1;
    }

    private static StackTraceElement getTraceElement() {
        StackTraceElement[] trace = Thread.currentThread().getStackTrace();
        int stackOffset = getStackOffset(trace) + 1;

        if (stackOffset > trace.length) {
            stackOffset = trace.length - 1;
        }
        return trace[stackOffset];

    }

    private static String getTag(@Nonnull StackTraceElement element) {
        return LogUtils.makeLogTag(getSimpleClassName(element.getClassName()));
    }

    private static String getMessage(@Nonnull StackTraceElement element, String msg) {
        return getSimpleClassName(element.getClassName()) +
                "." +
                element.getMethodName() +
                " " +
                " (" +
                element.getFileName() +
                ":" +
                element.getLineNumber() +
                ")" +
                " | " +
                msg;
    }

    public static void v(String msg) {
        StackTraceElement element = getTraceElement();
        String tag = getTag(element);
        if (LOGGING_ENABLED && Log.isLoggable(tag, Log.DEBUG)) {
            msg = getMessage(element, msg);
            LogImpl.v(tag, buildMessage(msg));
        }
    }

    public static void v(String msg, Throwable thr) {
        StackTraceElement element = getTraceElement();
        String tag = getTag(element);
        if (LOGGING_ENABLED && Log.isLoggable(tag, Log.DEBUG)) {
            msg = getMessage(element, msg);
            LogImpl.v(tag, buildMessage(msg), thr);
        }
    }

    public static void d(String msg) {
        StackTraceElement element = getTraceElement();
        String tag = getTag(element);
        if (LOGGING_ENABLED && Log.isLoggable(tag, Log.DEBUG)) {
            msg = getMessage(element, msg);
            LogImpl.d(tag, buildMessage(msg));
        }
    }

    public static void d(String msg, Throwable thr) {
        StackTraceElement element = getTraceElement();
        String tag = getTag(element);
        if (LOGGING_ENABLED && Log.isLoggable(tag, Log.DEBUG)) {
            msg = getMessage(element, msg);
            LogImpl.d(tag, buildMessage(msg), thr);
        }
    }

    public static void i(String msg) {
        StackTraceElement element = getTraceElement();
        String tag = getTag(element);
        if (LOGGING_ENABLED && Log.isLoggable(tag, Log.DEBUG)) {
            msg = getMessage(element, msg);
            LogImpl.i(tag, buildMessage(msg));
        }
    }

    public static void i(String msg, Throwable thr) {
        StackTraceElement element = getTraceElement();
        String tag = getTag(element);
        if (LOGGING_ENABLED && Log.isLoggable(tag, Log.DEBUG)) {
            msg = getMessage(element, msg);
            LogImpl.i(tag, buildMessage(msg), thr);
        }
    }

    public static void w(String msg) {
        StackTraceElement element = getTraceElement();
        String tag = getTag(element);
        msg = getMessage(element, msg);
        LogImpl.w(tag, buildMessage(msg));
    }

    public static void w(String msg, Throwable thr) {
        StackTraceElement element = getTraceElement();
        String tag = getTag(element);
        msg = getMessage(element, msg);
        LogImpl.w(tag, buildMessage(msg), thr);
    }

    public static void w(Throwable thr) {
        StackTraceElement element = getTraceElement();
        String tag = getTag(element);
        LogImpl.w(tag, buildMessage(""), thr);
    }

    public static void e(String msg) {
        StackTraceElement element = getTraceElement();
        String tag = getTag(element);
        msg = getMessage(element, msg);
        LogImpl.e(tag, buildMessage(msg));
    }

    public static void e(String msg, Throwable thr) {
        StackTraceElement element = getTraceElement();
        String tag = getTag(element);
        msg = getMessage(element, msg);
        LogImpl.e(tag, buildMessage(msg), thr);
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
