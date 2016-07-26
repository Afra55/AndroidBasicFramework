package com.afra55.apimodule.helper;

import com.afra55.apimodule.api.APIField;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Victor Yang on 2016/7/26.
 */
public class RetrofitHelper {

    public static Retrofit retrofit(String baseUrl) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static Retrofit retrofit() {
        return retrofit(APIField.getAppServiceUrl());
    }
}
