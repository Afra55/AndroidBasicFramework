package com.afra55.commontutils.network;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.convert.AnnotationStrategy;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.strategy.Strategy;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

/**
 * @author houwenpeng
 * @version V1.0
 * @Package 酷行
 * @Title com.hwp.framework.network
 * @date 16/9/21
 * @Description:
 */
public class RetrofitUtil {

    private static Retrofit mRetrofit;
    private static Retrofit mRetrofit2;
    private static OkHttpClient mOkHttpClient;
    public static String API_SERVER = "";
    public static boolean isDebug = true;
    /**
     * 获取retrofit实例
     * @return
     */
    public static <T> T  createService(Class<T> cla){
        if (null == mRetrofit) {
            if (null == mOkHttpClient) {
                mOkHttpClient = OkHttpClientUtil.getOkHttpClient();
            }
            mRetrofit = new Retrofit.Builder()
                    .baseUrl(API_SERVER)
                    .addConverterFactory(ResponseConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(mOkHttpClient)
                    .build();
        }
        return mRetrofit.create(cla);
    }

    public static <T> T createServiceXml(String base_url, Class<T> cla){
        if (null == mRetrofit2) {
            if (null == mOkHttpClient) {
                mOkHttpClient = OkHttpClientUtil.getOkHttpClient();
            }

            Strategy strategy = new AnnotationStrategy();
            Serializer serializer = new Persister(strategy);

            mRetrofit2 = new Retrofit.Builder()
                    .baseUrl(base_url)
                    .addConverterFactory(ResponseConverterXmlFactory.create(serializer))
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(mOkHttpClient)
                    .build();
        }
        return mRetrofit2.create(cla);
    }

    public static void setIsDebug(boolean isDebug) {
        RetrofitUtil.isDebug = isDebug;
    }
}