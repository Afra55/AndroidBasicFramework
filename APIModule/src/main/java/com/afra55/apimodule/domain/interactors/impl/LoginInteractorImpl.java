package com.afra55.apimodule.domain.interactors.impl;

import com.afra55.apimodule.domain.interactors.LoginInteractor;
import com.afra55.apimodule.domain.model.LoginBean;
import com.afra55.apimodule.domain.repository.LoginRepository;
import com.afra55.commontutils.base.AbstractInteractor;
import com.afra55.commontutils.base.Executor;
import com.afra55.commontutils.base.MainThread;
import com.afra55.commontutils.log.LogUtils;

/**
 * Created by yangshuai on 2017/5/12.
 */

public class LoginInteractorImpl extends AbstractInteractor implements LoginInteractor {

    private final String TAG = LogUtils.makeLogTag(LoginInteractorImpl.class);

    private long phone;
    private LoginInteractor.Callback mCallback;
    private LoginRepository repository;

    public LoginInteractorImpl(
            Executor threadExecutor
            , MainThread mainThread
            , LoginRepository repository
            , long phone
            , LoginInteractor.Callback callback) {
        super(threadExecutor, mainThread);
        this.repository = repository;
        this.phone = phone;
        this.mCallback = callback;
    }

    @Override
    public void run() {

        LogUtils.i(TAG, "start");


        mMainThread.post(new Runnable() {
            @Override
            public void run() {
                mCallback.onStart();
            }
        });

        LoginBean loginBean = repository.getLoginBean();
        if (loginBean == null) {
            loginBean = new LoginBean();
            loginBean.setId(phone);
            loginBean.setName("Afra55");
            loginBean.setToken("afra55_token");
            repository.insert(loginBean);
        } else {
            loginBean.setId(phone);
            loginBean.setName("FFFA");
            loginBean.setToken("FFFA_token");
            repository.update(loginBean);
        }

        final LoginBean finalLoginBean = loginBean;
        mMainThread.post(new Runnable() {
            @Override
            public void run() {
                mCallback.onLoginResultReturn(finalLoginBean);
            }
        });

        LogUtils.i(TAG, "end");
    }

    @Override
    protected void onCompleted() {
        LogUtils.i(TAG, "onCompleted");
        mMainThread.post(new Runnable() {
            @Override
            public void run() {
                mCallback.onCompleted();
            }
        });
    }
}
