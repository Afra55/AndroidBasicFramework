package com.afra55.apimodule.domain.interactors.impl;

import com.afra55.apimodule.domain.interactors.ToTranslateInteractor;
import com.afra55.apimodule.domain.model.TranslateBean;
import com.afra55.apimodule.services.APIServices;
import com.afra55.commontutils.base.APIField;
import com.afra55.commontutils.base.AbstractInteractor;
import com.afra55.commontutils.base.Executor;
import com.afra55.commontutils.base.MainThread;
import com.afra55.commontutils.http.BaseDisposableObserver;
import com.afra55.commontutils.http.RequestBody;
import com.afra55.commontutils.http.RequestQuery;
import com.afra55.commontutils.http.RetrofitHelper;
import com.afra55.commontutils.string.MD5;

import java.util.Random;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by yangshuai on 2017/5/10.
 */

public class ToTranslateInteractorImpl extends AbstractInteractor implements ToTranslateInteractor {

    private String text;
    private ToTranslateInteractor.Callback mCallback;

    public ToTranslateInteractorImpl(
            Executor threadExecutor
            , MainThread mainThread
            , String text
            , Callback callback) {
        super(threadExecutor, mainThread);
        this.text = text;
        this.mCallback = callback;
    }

    @Override
    protected boolean interceptFinishOperation() {
        return true;
    }

    @Override
    protected void onCompleted() {
        mMainThread.post(new Runnable() {
            @Override
            public void run() {
                mCallback.onCompleted();
            }
        });
    }

    @Override
    public void run() {
        int salt = new Random(100).nextInt();
        RetrofitHelper.createService(APIServices.class)
                .toTranslate(
                        APIField.OtherHttp.TRANSLATE_HOST + APIField.OtherHttp.TRANSLATE_API
                        ,new RequestQuery.Build()
                                .withParam("q", text)
                                .withParam("from", "auto")
                                .withParam("to", "auto")
                                .withParam("appid", APIField.OtherHttp.APPID)
                                .withParam("salt", String.valueOf(salt))
                                .withParam("sign", MD5.getStringMD5(APIField.OtherHttp.APPID
                                        + text + salt + APIField.OtherHttp.SECRET))
                                .build()
                        , new RequestBody.Builder()
//                                .withObject(JSON.toJSONString(info))
                                .build())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(addSubscriber(new BaseDisposableObserver<TranslateBean>() {
                    @Override
                    public void onSuccess(final TranslateBean info) {
                        mMainThread.post(new Runnable() {
                            @Override
                            public void run() {
                                mCallback.onTranslateResultReturn(info);
                            }
                        });
                    }

                    @Override
                    public void onFailure(int errorCode, String errorMsg) {
                        mMainThread.post(new Runnable() {
                            @Override
                            public void run() {
                                mCallback.onTranslateResultReturn(null);
                            }
                        });
                    }

                    @Override
                    public void onStart() {
                        super.onStart();
                        mMainThread.post(new Runnable() {
                            @Override
                            public void run() {
                                mCallback.onStart();
                            }
                        });
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        onFinished(true);
                    }

                }));

    }
}
