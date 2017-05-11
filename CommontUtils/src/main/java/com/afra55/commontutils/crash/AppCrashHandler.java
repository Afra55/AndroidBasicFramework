package com.afra55.commontutils.crash;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.widget.Toast;

import com.afra55.commontutils.AppCache;
import com.afra55.commontutils.R;
import com.afra55.commontutils.log.LogUtils;

import java.lang.Thread.UncaughtExceptionHandler;

public class AppCrashHandler implements Thread.UncaughtExceptionHandler{

    private static final String TAG = AppCrashHandler.class.getSimpleName();

	private Application application;

    private Class mRestartClass;

	private UncaughtExceptionHandler uncaughtExceptionHandler;

	private static AppCrashHandler instance;

	private AppCrashHandler(Application context, Class restartClass) {
		this.application = context;
        mRestartClass = restartClass;

		// get default
		uncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
		
		// install
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	public static AppCrashHandler getInstance(Application mContext, Class restartClass) {
		if (instance == null) {
			instance = new AppCrashHandler(mContext, restartClass);
		}
		
		return instance;
	}

	public final void saveException(Throwable ex, boolean uncaught) {
		CrashSaver.save(application, ex, uncaught);
	}
	
	public void setUncaughtExceptionHandler(UncaughtExceptionHandler handler) {
		if (handler != null) {
			this.uncaughtExceptionHandler = handler;
		}
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		// save log
		saveException(ex, true);
        if (AppCache.isDebug()) {
            LogUtils.e(thread.getName(), thread.toString(), ex);
        }

        if (application.getPackageName().equals(getProcessName(application)))  {

            if (AppCache.isDebug()) {
                uncaughtExceptionHandler.uncaughtException(thread, ex);
            } else {
                toastSorry();
                restart();
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        } else {
            android.os.Process.killProcess(android.os.Process.myPid());
        }
	}

    private void toastSorry() {
        new Thread(){
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(
                        application.getApplicationContext(),
                        R.string.app_breakdown,
                        Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        }.start();
    }

    public static String getProcessName(Context appContext) {
        String currentProcessName = "";
        int pid = android.os.Process.myPid();
        ActivityManager manager = (ActivityManager) appContext.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo processInfo : manager.getRunningAppProcesses()) {
            if (processInfo.pid == pid) {
                currentProcessName = processInfo.processName;
                break;
            }
        }
        return currentProcessName;
    }

    /* 重启应用 */
    private void restart() {
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            android.util.Log.e(TAG, "error : " + e);
        }
        Application mApplication = (Application) application.getApplicationContext();
        Intent intent = new Intent(
                mApplication.getApplicationContext(),
                mRestartClass);
        PendingIntent restartIntent = PendingIntent.getActivity(
                mApplication.getApplicationContext(), 0, intent,
                Intent.FLAG_ACTIVITY_NEW_TASK);
        //退出程序
        AlarmManager mgr = (AlarmManager) mApplication.getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000,
                restartIntent); // 1秒钟后重启应用

    }
}
