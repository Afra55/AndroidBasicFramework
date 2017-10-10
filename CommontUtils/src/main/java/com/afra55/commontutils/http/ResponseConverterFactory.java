package com.afra55.commontutils.http;

import org.json.JSONObject;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by yangshuai on 2017/8/19.
 * {link http://afra55.github.io}
 */

public class ResponseConverterFactory extends Converter.Factory {

    public static ResponseConverterFactory create() {
        return create(new JSONObject());
    }

    private static ResponseConverterFactory create(JSONObject jsonObject) {
        return new ResponseConverterFactory(jsonObject);
    }

    private ResponseConverterFactory(JSONObject jsonObject) {
        if (jsonObject == null) throw new NullPointerException("json == null");
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type,
                                                            Annotation[] annotations,
                                                            Retrofit retrofit) {
        return new JsonResponseBodyConverter<>(type);
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type,
                                                          Annotation[] parameterAnnotations,
                                                          Annotation[] methodAnnotations,
                                                          Retrofit retrofit) {
        return new JsonRequestBodyConverter<>();
    }

}
