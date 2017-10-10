package com.afra55.commontutils.http;

import com.afra55.commontutils.AppCache;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by yangshuai on 2017/8/19.
 * {link http://afra55.github.io}
 */
public class OkHttpClientUtil {

    private static OkHttpClient mOkHttpClient;
    private static long SecondTime = 1000;
    //设置缓存目录
    private static File cacheDirectory = new File(AppCache.getContext().getCacheDir().getAbsolutePath(), "MyCache");
    private static Cache cache = new Cache(cacheDirectory, 10 * 1024 * 1024);


    /**
     * 获取OkHttpClient对象
     *
     * @return
     */
    public static OkHttpClient getOkHttpClient() {
        if (null == mOkHttpClient) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            if (AppCache.isDebug()) {
                loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            }
            mOkHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(new TokenInterceptor())
                    .addNetworkInterceptor(new StethoInterceptor())
                    //设置请求读写的超时时间
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .addInterceptor(loggingInterceptor)
                    .cache(cache)
                    .build();
        }
        return mOkHttpClient;
    }

    /**
     * 自定义链接地址中包含token的添加
     */
    private static class TokenInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request.Builder newBuilder = chain.request().newBuilder();
            HttpUrl url = chain.request().url();
            if (url!=null && url.toString().contains("?")){
                newBuilder.url(url + "&token=" + AppCache.getToken());
            }else{
                newBuilder.url(url + "?token=" + AppCache.getToken());

            }
            return chain.proceed(newBuilder.build());
        }
    }


}
