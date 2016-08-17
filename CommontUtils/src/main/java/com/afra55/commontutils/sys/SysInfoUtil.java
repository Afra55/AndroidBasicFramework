package com.afra55.commontutils.sys;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.PowerManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;

public class SysInfoUtil {
	public static final String getOsInfo() {
		return Build.VERSION.RELEASE;
	}

	public static final String getPhoneModelWithManufacturer() {
		return Build.MANUFACTURER + " " + Build.MODEL;
	}

    public static final String getPhoneMode() {
        return Build.MODEL;
    }

	public static final boolean isAppOnForeground(Context context) {
		ActivityManager manager = (ActivityManager) context
				.getApplicationContext().getSystemService(
						Context.ACTIVITY_SERVICE);
		String packageName = context.getApplicationContext().getPackageName();
		List<ActivityManager.RunningAppProcessInfo> list = manager
				.getRunningAppProcesses();
		if (list == null)
			return false;
		boolean ret = false;
		Iterator<ActivityManager.RunningAppProcessInfo> it = list.iterator();
		while (it.hasNext()) {
			ActivityManager.RunningAppProcessInfo appInfo = it.next();
			if (appInfo.processName.equals(packageName)
					&& appInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
				ret = true;
				break;
			}
		}
		return ret;
	}

	/**
	 * 判断当前App处于前台还是后台
	 * <p>需添加权限 android.permission.GET_TASKS</p>
	 * <p>并且必须是系统应用该方法才有效</p>
	 *
	 * @param context 上下文
	 * @return true: 后台<br>false: 前台
	 */
	public static boolean isAppBackground(Context context) {
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		@SuppressWarnings("deprecation")
		List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
		if (!tasks.isEmpty()) {
			ComponentName topActivity = tasks.get(0).topActivity;
			if (!topActivity.getPackageName().equals(context.getPackageName())) {
				return true;
			}
		}
		return false;
	}

	public static final boolean isScreenOn(Context context) {
		PowerManager powerManager = (PowerManager) context
				.getSystemService(Context.POWER_SERVICE);
		return powerManager.isScreenOn();
	}
	
	public static boolean stackResumed(Context context) {
		ActivityManager manager = (ActivityManager) context
				.getApplicationContext().getSystemService(
						Context.ACTIVITY_SERVICE);
		String packageName = context.getApplicationContext().getPackageName();
		List<RunningTaskInfo> recentTaskInfos = manager.getRunningTasks(1);
		if (recentTaskInfos != null && recentTaskInfos.size() > 0) {
			RunningTaskInfo taskInfo = recentTaskInfos.get(0);
			if (taskInfo.baseActivity.getPackageName().equals(packageName) && taskInfo.numActivities > 1) {
				return true;
			}
		}
		
		return false;
	}

    /**
     * 判断是否运行在模拟器上
     * @param context
     * @return
     */
	public static final boolean mayOnEmulator(Context context) {
		if (mayOnEmulatorViaBuild()) {
			return true;
		}
		
		if (mayOnEmulatorViaTelephonyDeviceId(context)) {
			return true;
		}

		if (mayOnEmulatorViaQEMU(context)) {
			return true;
		}

		return false;
	}
	
	private static final boolean mayOnEmulatorViaBuild() {
		/**
		 * ro.product.model likes sdk
		 */
		if (!TextUtils.isEmpty(Build.MODEL) && Build.MODEL.toLowerCase().contains("sdk")) {
			return true;
		}
		
		/**
		 * ro.product.manufacturer likes unknown
		 */
		if (!TextUtils.isEmpty(Build.MANUFACTURER) && Build.MANUFACTURER.toLowerCase().contains("unknown")) {
			return true;
		}

		/**
		 * ro.product.device likes generic
		 */
		if (!TextUtils.isEmpty(Build.DEVICE) && Build.DEVICE.toLowerCase().contains("generic")) {
			return true;
		}
	
		return false;
	}
	
	private static final boolean mayOnEmulatorViaTelephonyDeviceId(Context context) {		
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		if (tm == null) {
			return false;
		}
		
		String deviceId = tm.getDeviceId();
		if (TextUtils.isEmpty(deviceId)) {
			return false;
		}
		
		/**
		 * device id of telephony likes '0*'
		 */
		for (int i = 0; i < deviceId.length(); i++) {
			if (deviceId.charAt(i) != '0') {
				return false;
			}
		}
		
		return true;
	}
	
	private static final boolean mayOnEmulatorViaQEMU(Context context) {
		String qemu = getProp(context, "ro.kernel.qemu");
		return "1".equals(qemu);
	}
	
	private static final String getProp(Context context, String property) {
    	try {
    		ClassLoader cl = context.getClassLoader();
    		Class<?> SystemProperties = cl.loadClass("android.os.SystemProperties");
    		Method method = SystemProperties.getMethod("get", String.class);
    		Object[] params = new Object[1];
    		params[0] = property;
    		return (String)method.invoke(SystemProperties, params);
    	} catch (Exception e) {
    		return null;
    	}
    }

	/**
	 * 判断 sdk_int 是否小于等于系统版本号
	 *
	 * @param sdk_int
	 * @return
	 */
	public static boolean isCompatible(int sdk_int) {
		return Build.VERSION.SDK_INT >= sdk_int;
	}


    /**
     * 获取设备MAC地址
     * <p>需添加权限 android.permission.ACCESS_WIFI_STATE</p>
     *
     * @param context 上下文
     * @return MAC地址
     */
    public static String getMacAddress(Context context) {
        WifiManager wifi = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        String macAddress = info.getMacAddress().replace(":", "");
        return macAddress == null ? "" : macAddress;
    }

    /**
     * 获取设备MAC地址
     * <p>需添加权限 android.permission.ACCESS_WIFI_STATE</p>
     *
     * @return MAC地址
     */
    public static String getMacAddress() {
        String macAddress = null;
        LineNumberReader reader = null;
        try {
            Process pp = Runtime.getRuntime().exec("cat /sys/class/net/wlan0/address");
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            reader = new LineNumberReader(ir);
            macAddress = reader.readLine().replace(":", "");
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (reader != null) reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return macAddress == null ? "" : macAddress;
    }

    /**
     * 获取设备厂商，如Xiaomi
     *
     * @return 设备厂商
     */
    public static String getManufacturer() {
        return Build.MANUFACTURER;
    }

    /**
     * 获取设备型号，如MI2SC
     *
     * @return 设备型号
     */
    public static String getModel() {
        String model = Build.MODEL;
        if (model != null) {
            model = model.trim().replaceAll("\\s*", "");
        } else {
            model = "";
        }
        return model;
    }
}
