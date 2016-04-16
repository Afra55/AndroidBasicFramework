package com.afra55.baseclient.base;

import android.app.Application;

import com.facebook.common.internal.Supplier;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.backends.okhttp.OkHttpImagePipelineConfigFactory;
import com.facebook.imagepipeline.cache.MemoryCacheParams;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.squareup.okhttp.OkHttpClient;


/**
 * Created by yangshuai in the 10:37 of 2016.01.05 .
 */
public class CusstomApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

//        OkHttpClient okHttpClient = new OkHttpClient();
//
//        Supplier<MemoryCacheParams> bitmapCacheParamsSupplier = new Supplier<MemoryCacheParams>() {
//            @Override
//            public MemoryCacheParams get() {
//                return new MemoryCacheParams();
//            }
//        };
//
//        ImagePipelineConfig config = OkHttpImagePipelineConfigFactory
//                .newBuilder(getApplicationContext(), okHttpClient)
//                .setBitmapMemoryCacheParamsSupplier(bitmapCacheParamsSupplier)
//                .build();
//        Fresco.initialize(this, config);

        /* 异常捕获(debug 时不捕获异常) */
        Thread.setDefaultUncaughtExceptionHandler(new MyUncaughtExceptionHandler(this));

    }
}
