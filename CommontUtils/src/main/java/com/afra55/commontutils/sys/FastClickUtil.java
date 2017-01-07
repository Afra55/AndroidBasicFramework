package com.afra55.commontutils.sys;

public class FastClickUtil {

    private static long lastClickTime;

    /**
     * 防暴力点击
     * @param dur 时间间隔
     * @return true:多次点击
     */
    public static boolean isFastDoubleClick(long dur) {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;

        if (0 < timeD && timeD < dur) {
            lastClickTime = time;
            return true;
        }
        lastClickTime = time;
        return false;
    }

    /**
     * 防暴力点击
     *
     * @return true:多次点击，不处理
     */
    public static boolean isFastDoubleClick() {
        return isFastDoubleClick(500);
    }

}
