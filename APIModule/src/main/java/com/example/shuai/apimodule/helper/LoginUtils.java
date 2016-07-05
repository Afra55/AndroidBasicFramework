package com.example.shuai.apimodule.helper;

/**
 * Created by Victor Yang on 2016/6/30.
 */
public class LoginUtils {

    private LoginUtils(){}

    private static class LoginUtilsHolder{
        public static final LoginUtils sInstance = new LoginUtils();
    }

    public static LoginUtils getInstance(){
        return LoginUtilsHolder.sInstance;
    }

    /**
     * 已经登陆过，自动登陆
     */
    public boolean canAutoLogin() {
        // 处理
        return false;
    }

}
