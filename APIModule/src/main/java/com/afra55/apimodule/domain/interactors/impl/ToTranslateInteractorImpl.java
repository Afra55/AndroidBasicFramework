package com.afra55.apimodule.domain.interactors.impl;

import com.afra55.apimodule.api.APIServices;
import com.afra55.apimodule.domain.interactors.ToTranslateInteractor;
import com.afra55.apimodule.domain.model.TranslateBean;
import com.afra55.commontutils.base.APIField;
import com.afra55.commontutils.base.AbstractInteractor;
import com.afra55.commontutils.base.Executor;
import com.afra55.commontutils.base.MainThread;
import com.afra55.commontutils.network.DataCoverSubscriber;
import com.afra55.commontutils.network.Request;
import com.afra55.commontutils.network.RequestQuery;
import com.afra55.commontutils.network.RetrofitUtil;
import com.afra55.commontutils.string.MD5;

import java.util.Random;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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
    public void run() {
        int salt = new Random(100).nextInt();
        RetrofitUtil.createService(APIServices.class)
                .toTranslate(
                        APIField.OtherHttp.TRANSLATE_HOST + APIField.OtherHttp.TRANSLATE_API
                        ,new RequestQuery.Build()
                                .withParams("q", text)
                                .withParams("from", "auto")
                                .withParams("to", "auto")
                                .withParams("appid", APIField.OtherHttp.APPID)
                                .withParams("salt", String.valueOf(salt))
                                .withParams("sign", MD5.getStringMD5(APIField.OtherHttp.APPID
                                        + text + salt + APIField.OtherHttp.SECRET))
                                .build()
                        ,new Request.Builder()
//                                .withObject(JSON.toJSONString(info))
                                .build())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(addSubscriber(new DataCoverSubscriber<TranslateBean>() {
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
                    }

                    @Override
                    public void onCompleted() {

                    }
                }));

    }
}
