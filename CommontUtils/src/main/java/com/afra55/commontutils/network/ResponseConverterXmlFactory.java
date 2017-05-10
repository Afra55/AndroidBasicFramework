package com.afra55.commontutils.network;

import com.hwp.framework.util.LogUtils;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okio.Buffer;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * @author houwenpeng
 * @version V1.0
 * @Package 酷行
 * @Title com.hwp.framework.network
 * @date 16/9/26
 * @Description:
 */
public class ResponseConverterXmlFactory extends Converter.Factory {
    /** Create an instance using a default {@link Persister} instance for conversion. */
    public static ResponseConverterXmlFactory create() {
        return create(new Persister());
    }

    /** Create an instance using {@code serializer} for conversion. */
    public static ResponseConverterXmlFactory create(Serializer serializer) {
        return new ResponseConverterXmlFactory(serializer, true);
    }

    /** Create an instance using a default {@link Persister} instance for non-strict conversion. */
    public static ResponseConverterXmlFactory createNonStrict() {
        return createNonStrict(new Persister());
    }

    /** Create an instance using {@code serializer} for non-strict conversion. */
    public static ResponseConverterXmlFactory createNonStrict(Serializer serializer) {
        return new ResponseConverterXmlFactory(serializer, false);
    }

    private final Serializer serializer;
    private final boolean strict;

    private ResponseConverterXmlFactory(Serializer serializer, boolean strict) {
        if (serializer == null) throw new NullPointerException("serializer == null");
        this.serializer = serializer;
        this.strict = strict;
    }

    public boolean isStrict() {
        return strict;
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations,
                                                            Retrofit retrofit) {
        if (!(type instanceof Class)) {
            return null;
        }
        Class<?> cls = (Class<?>) type;
        return new SimpleXmlResponseBodyConverter<>(cls, serializer, strict);
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type,
                                                          Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        if (!(type instanceof Class)) {
            return null;
        }
        return new SimpleXmlRequestBodyConverter<>(serializer);
    }
}

final class SimpleXmlResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Class<T> cls;
    private final Serializer serializer;
    private final boolean strict;

    SimpleXmlResponseBodyConverter(Class<T> cls, Serializer serializer, boolean strict) {
        this.cls = cls;
        this.serializer = serializer;
        this.strict = strict;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        try {
            T read = serializer.read(cls, value.byteStream(), strict);
            if (read == null) {
                throw new IllegalStateException("Could not deserialize body as " + cls);
            }
            return read;
        } catch (RuntimeException | IOException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            value.close();
        }
    }
    private void tos(InputStream is){
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "/n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        LogUtils.d("hwp","================"+sb.toString());
    }

}


final class SimpleXmlRequestBodyConverter<T> implements Converter<T, RequestBody> {
    private static final MediaType MEDIA_TYPE = MediaType.parse("application/xml; charset=UTF-8");
    private static final String CHARSET = "UTF-8";

    private final Serializer serializer;

    SimpleXmlRequestBodyConverter(Serializer serializer) {
        this.serializer = serializer;
    }

    @Override
    public RequestBody convert(T value) throws IOException {
        Buffer buffer = new Buffer();
        try {
            OutputStreamWriter osw = new OutputStreamWriter(buffer.outputStream(), CHARSET);
            serializer.write(value, osw);
            osw.flush();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return RequestBody.create(MEDIA_TYPE, buffer.readByteString());
    }
}
