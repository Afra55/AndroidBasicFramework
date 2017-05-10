package com.afra55.apimodule.domain.interactors.impl;

import com.afra55.apimodule.api.APIField;
import com.afra55.apimodule.api.APIServices;
import com.afra55.apimodule.domain.executor.Executor;
import com.afra55.apimodule.domain.executor.MainThread;
import com.afra55.apimodule.domain.interactors.ToTranslateInteractor;
import com.afra55.apimodule.domain.interactors.base.AbstractInteractor;
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

public class ToTransltateInteractorImpl extends AbstractInteractor implements ToTranslateInteractor {

    private String text;

    public ToTransltateInteractorImpl(
            Executor threadExecutor
            , MainThread mainThread
            , String text) {
        super(threadExecutor, mainThread);
        this.text = text;
    }

    @Override
    public void run() {
        int salt = new Random(100).nextInt();
        RetrofitUtil.createService(APIServices.class)
                .toTranslate(new RequestQuery.Build()
                                .withParams("q", text)
                                .withParams("from", "auto")
                                .withParams("to", "auto")
                                .withParams("appid", APIField.OtherHttp.APPID)
                                .withParams("salt", String.valueOf(salt))
                                .withParams("sign", MD5.getStringMD5(APIField.OtherHttp.APPID
                                        + text + salt + APIField.OtherHttp.SECRET))
                                .build(),
                        new Request.Builder()
//                                .withObject(JSON.toJSONString(info))
                                .build())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(addSubscriber(new DataCoverSubscriber<Object>() {
                    @Override
                    public void onSuccess(Object info) {

                    }

                    @Override
                    public void onFailure(int errorCode, String errorMsg) {
                    }
                }));

    }
}
