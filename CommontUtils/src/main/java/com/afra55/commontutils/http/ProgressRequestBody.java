package com.afra55.commontutils.http;

import android.support.annotation.NonNull;

import java.io.File;
import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DefaultObserver;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

/**
 * @author Afra55
 * @date 2018/4/16
 * A smile is the best business card.
 */
public class ProgressRequestBody extends okhttp3.RequestBody {
    /**
     * 实际的待包装请求体
     **/
    private final okhttp3.RequestBody requestBody;
    /**
     * 进度回调接口
     **/
    private final UploadProgressListener progressListener;
    /**
     * 包装完成的BufferedSink
     **/
    private BufferedSink bufferedSink;

    public ProgressRequestBody(okhttp3.RequestBody requestBody, UploadProgressListener progressListener) {
        this.requestBody = requestBody;
        this.progressListener = progressListener;
    }

    public static MultipartBody.Part createPartOfMultipartBody(
            String param,
            String fileName,
            RequestBody requestBody,
            UploadProgressListener uploadProgressListener) {
        return MultipartBody.Part.createFormData(
                param,
                fileName,
                new ProgressRequestBody(requestBody, uploadProgressListener));
    }

    public static MultipartBody.Part createPartOfMultipartBody(
            String param,
            String fileName,
            String mediaType,
            @NonNull File file,
            UploadProgressListener uploadProgressListener) {
        return MultipartBody.Part.createFormData(
                param,
                fileName,
                new ProgressRequestBody(
                        RequestBody.create(MediaType.parse(mediaType), file),
                        uploadProgressListener));
    }

    public static MultipartBody.Part createPartOfMultipartBody(
            String param,
            String mediaType,
            @NonNull File file,
            UploadProgressListener uploadProgressListener) {
        return MultipartBody.Part.createFormData(
                param,
                file.getName(),
                new ProgressRequestBody(
                        RequestBody.create(MediaType.parse(mediaType), file),
                        uploadProgressListener));
    }

    /**
     * 重写调用实际的响应体的contentType
     *
     * @return MediaType
     */
    @Override
    public MediaType contentType() {
        return requestBody.contentType();
    }

    /**
     * 重写调用实际的响应体的contentLength
     *
     * @return contentLength
     * @throws IOException 异常
     */
    @Override
    public long contentLength() throws IOException {
        return requestBody.contentLength();
    }

    /**
     * 重写进行写入
     *
     * @param sink BufferedSink
     * @throws IOException 异常
     */
    @Override
    public void writeTo(@NonNull BufferedSink sink) throws IOException {
        if (null == bufferedSink) {
            bufferedSink = Okio.buffer(sink(sink));
        }
        requestBody.writeTo(bufferedSink);
        //必须调用flush，否则最后一部分数据可能不会被写入
        bufferedSink.flush();
    }

    /**
     * 写入，回调进度接口
     *
     * @param sink Sink
     * @return Sink
     */
    private Sink sink(Sink sink) {
        return new ForwardingSink(sink) {
            //当前写入字节数
            long writtenBytesCount = 0L;
            //总字节长度，避免多次调用contentLength()方法
            long totalBytesCount = 0L;

            @Override
            public void write(@NonNull Buffer source, long byteCount) throws IOException {
                super.write(source, byteCount);
                //增加当前写入的字节数
                writtenBytesCount += byteCount;
                //获得contentLength的值，后续不再调用
                if (totalBytesCount == 0) {
                    totalBytesCount = contentLength();
                }
                Observable.just(writtenBytesCount).observeOn(AndroidSchedulers.mainThread()).subscribe(new DefaultObserver<Long>() {
                    @Override
                    public void onNext(Long aLong) {
                        progressListener.onProgress(writtenBytesCount, totalBytesCount);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
            }
        };
    }
}