package com.afra55.commontutils.field;

/**
 * Created by yangshuai in the 9:47 of 2016.01.07 .
 */
public class APIField {

    static {
        // 切换环境
        useUAT();
    }

    private static boolean isQA = true;

    private static String APP_SERVICE_URL;

    public static String getAppServiceUrl() {
        return APP_SERVICE_URL;
    }

    /**
     * 测试环境
     */
    private static void useUAT() {
        isQA = true;
        APP_SERVICE_URL = "www.baidu.com";
    }

    /**
     * 产线环境
     */
    private static void usePro() {
        isQA = false;
        APP_SERVICE_URL = "www.google.com";
    }

    public interface IAD{
        public static final String INDEX_AD_XXX = APIField.APP_SERVICE_URL + "广告地址";
    }

    public interface OtherHttp{
        public static final String TRANSLATE = "http://api.fanyi.baidu.com/api/trans/vip/translate";
    }
}
