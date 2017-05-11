package com.afra55.commontutils.network;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okio.Buffer;
import retrofit2.Converter;
import retrofit2.Retrofit;

public class ResponseConverterFactory extends Converter.Factory {

    private JSONObject jsonObject;

    public static ResponseConverterFactory create() {
        return create(new JSONObject());
    }

    private static ResponseConverterFactory create(JSONObject jsonObject) {
        return new ResponseConverterFactory(jsonObject);
    }

    private ResponseConverterFactory(JSONObject jsonObject) {
        if (jsonObject == null) throw new NullPointerException("json == null");
        this.jsonObject = jsonObject;
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

class JsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private Type mType;

    public JsonResponseBodyConverter(Type type) {
        this.mType = type;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        try {
            //解析数据
            BufferedReader br = new BufferedReader(value.charStream());
            String line;
            StringBuilder buffer = new StringBuilder();
            while ((line = br.readLine()) != null) {
                buffer.append(line);
            }
            String result = buffer.toString();
            Result response = JSON.parseObject(result, Result.class);
            if (TextUtils.isEmpty(response.getBizData())
                    && TextUtils.isEmpty(response.getRtnCode())) {
                return JSON.parseObject(result, mType);
            }
            if (value.contentType().subtype().equals("html")) {
                return (T) response;
            } else {
                try {
                    if (response.isSuccess()) {
                        try {
                            return JSON.parseObject(response.getBizData(), mType);
                        } catch (Exception e) {
                            try {
                                return JSON.parseObject(JSON.toJSONString(response.getBizData()), mType);
                            } catch (Exception e1) {
                                return new Gson().fromJson(response.getBizData(), mType);
                            }
                        }
                    } else {
                        throw new HttpRuntimeException(response.getRtnCode(), response.getMsg());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new HttpRuntimeException(response.getRtnCode(), response.getMsg());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new HttpRuntimeException(((HttpRuntimeException) e).getErrorCode(), ((HttpRuntimeException) e).getErrorMsg());
        }
    }
}

final class JsonRequestBodyConverter<T> implements Converter<T, RequestBody> {
    private static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8");
    private static final Charset UTF_8 = Charset.forName("UTF-8");

    @Override
    public RequestBody convert(T value) throws IOException {
        try {
            Buffer buffer = new Buffer();
            Writer writer = new OutputStreamWriter(buffer.outputStream(), UTF_8);
            writer.write(value.toString());
            writer.close();
            return RequestBody.create(MEDIA_TYPE, buffer.readByteString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}