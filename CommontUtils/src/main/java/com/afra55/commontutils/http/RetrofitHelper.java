package com.afra55.commontutils.http;

import com.afra55.commontutils.AppCache;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.convert.AnnotationStrategy;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.strategy.Strategy;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * Created by Victor Yang on 2016/7/26.
 * {link http://afra55.github.io}
 */
public class RetrofitHelper {

    private static Retrofit mJsonRetrofit;
    private static Retrofit mXmlRetrofit;
    private static OkHttpClient mOkHttpClient;


    public static <T> T createService(Class<T> cls) {
        return createService(AppCache.getAppServiceUrl(), cls);
    }

    public static <T> T createService(String baseUrl, Class<T> cla) {
        if (null == mJsonRetrofit) {
            if (null == mOkHttpClient) {
                mOkHttpClient = OkHttpClientUtil.getOkHttpClient();
            }
            mJsonRetrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(ResponseConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(mOkHttpClient)
                    .build();
        }
        return mJsonRetrofit.create(cla);
    }

    public static <T> T createServiceXml(String base_url, Class<T> cla) {
        if (null == mXmlRetrofit) {
            if (null == mOkHttpClient) {
                mOkHttpClient = OkHttpClientUtil.getOkHttpClient();
            }

            Strategy strategy = new AnnotationStrategy();
            Serializer serializer = new Persister(strategy);

            mXmlRetrofit = new Retrofit.Builder()
                    .baseUrl(base_url)
                    .addConverterFactory(ResponseConverterXmlFactory.create(serializer))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(mOkHttpClient)
                    .build();
        }
        return mXmlRetrofit.create(cla);
    }

}
