package com.afra55.commontutils.network;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonWriter;

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

public class ResponseGsonConverterFactory extends Converter.Factory {


    public static ResponseGsonConverterFactory create() {
        return create(new Gson());
    }

    /**
     * Create an instance using {@code gson} for conversion. Encoding to JSON and
     * decoding from JSON (when no charset is specified by a header) will use UTF-8.
     */
    public static ResponseGsonConverterFactory create(Gson gson) {
        return new ResponseGsonConverterFactory(gson);
    }

    private final Gson gson;

    private ResponseGsonConverterFactory(Gson gson) {
        if (gson == null) throw new NullPointerException("gson == null");
        this.gson = gson;
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type,
                                                            Annotation[] annotations,
                                                            Retrofit retrofit) {
        return new GsonResponseBodyConverter<>(gson, type);
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type,
                                                          Annotation[] parameterAnnotations,
                                                          Annotation[] methodAnnotations,
                                                          Retrofit retrofit) {
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        return new GsonRequestBodyConverter<>(gson, adapter);
    }

}

class GsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private Gson mGson;
    private Type mType;

    public GsonResponseBodyConverter(Gson gson, Type type) {
        this.mGson = gson;
        this.mType = type;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        //解析数据
        if(value.toString() != null){
            Result response = JSON.parseObject(value.toString(),Result.class);
            if(value.contentType().subtype().equals("html")){
                return (T) response;
            }else{
                try {
                    if (response!=null && response.isSuccess()){
                        return JSON.parseObject(response.getBizData(),mType);
                    }else{
                        new HttpRuntimeException(response.getRtnCode(),response.getMsg());
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    new HttpRuntimeException(response.getRtnCode(),response.getMsg());
                }
            }
        }
        return null;
    }
}


final class GsonRequestBodyConverter<T> implements Converter<T, RequestBody> {
    private static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8");
    private static final Charset UTF_8 = Charset.forName("UTF-8");

    private final Gson gson;
    private final TypeAdapter<T> adapter;

    GsonRequestBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    @Override
    public RequestBody convert(T value) throws IOException {
        Buffer buffer = new Buffer();
        Writer writer = new OutputStreamWriter(buffer.outputStream(), UTF_8);
        JsonWriter jsonWriter = gson.newJsonWriter(writer);
        adapter.write(jsonWriter, value);
        jsonWriter.close();
        return RequestBody.create(MEDIA_TYPE, buffer.readByteString());
    }
}

