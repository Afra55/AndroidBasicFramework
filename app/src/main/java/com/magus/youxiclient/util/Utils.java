package com.magus.youxiclient.util;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.util.Enumeration;
import java.util.HashMap;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.google.gson.Gson;

public class Utils {


    /**
     * 检查是否有能上网的网络连接存在
     *
     * @param mContext 当前Activity
     * @return
     */
    public static boolean isNetWorkConnected(Context mContext) {
        // connectivity [,k?nek'tiv?ti]n.[数][电][计]连�?�?
        ConnectivityManager manager = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        return info != null && info.isConnected();
    }

    private static Gson gson = null;

    /**
     * json数据转换成传入class类型的bean
     */
    public synchronized static <T> T getJsonToBean(String json, Class<T> clazz) {
        T t = null;
        if (gson == null) {
            gson = new Gson();
        }
        t = gson.fromJson(json, clazz);
        return t;
    }

    public static String sha1(String str) {
        if (str == null || str.length() == 0) {
            return null;
        }

        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

        try {
            MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
            mdTemp.update(str.getBytes());

            byte[] md = mdTemp.digest();
            int j = md.length;
            char buf[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
                buf[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(buf);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 封装链接上的键值对并保存到hashmap
     *
     * @param url
     * @return HashMap<key,value>
     */
    public static HashMap<String, String> handleUrlParams(String url) {
        HashMap<String, String> hm = new HashMap<String, String>();
        if (url.contains("?") && url.indexOf("?") != url.length() - 1) {
            String params = url.substring(url.indexOf("?") + 1);
            if (params.contains("&")) {
                String[] paramArr = params.split("&");
                for (int i = 0; i < paramArr.length; i++) {
                    String str = paramArr[i];
                    if ((str.split("=")).length > 1) {
                        try {
                            hm.put(str.split("=")[0], URLDecoder.decode(str.split("=")[1], "UTF-8"));
                        } catch (UnsupportedEncodingException e) {
                        }
                    }
                }
            } else {
                if (params.contains("=")) {
                    try {
                        hm.put(params.split("=")[0], URLDecoder.decode(params.split("=")[1], "UTF-8"));
                    } catch (UnsupportedEncodingException e) {
                    }
                }
            }
        }
        return hm;
    }

    public static String getMobileIP(Context context) {
        //获取wifi服务
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        //判断wifi是否开启
        if (!wifiManager.isWifiEnabled()) {

        }
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        return intToIp(ipAddress);
    }

    private static String intToIp(int i) {

        return (i & 0xFF) + "." +
                ((i >> 8) & 0xFF) + "." +
                ((i >> 16) & 0xFF) + "." +
                (i >> 24 & 0xFF);
    }

    public static String getLocalIpAddress() {
        String IP = null;
        StringBuilder IPStringBuilder = new StringBuilder();
        try {
            Enumeration<NetworkInterface> networkInterfaceEnumeration = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaceEnumeration.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaceEnumeration.nextElement();
                Enumeration<InetAddress> inetAddressEnumeration = networkInterface.getInetAddresses();
                while (inetAddressEnumeration.hasMoreElements()) {
                    InetAddress inetAddress = inetAddressEnumeration.nextElement();
                    if (!inetAddress.isLoopbackAddress() &&
                            !inetAddress.isLinkLocalAddress() &&
                            inetAddress.isSiteLocalAddress()) {
                        IPStringBuilder.append(inetAddress.getHostAddress().toString());
                    }
                }
            }
        } catch (SocketException ex) {

        }

        IP = IPStringBuilder.toString();
        return IP;

    }

}
