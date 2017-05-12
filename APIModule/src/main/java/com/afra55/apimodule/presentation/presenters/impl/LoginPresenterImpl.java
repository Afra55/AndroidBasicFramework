package com.afra55.apimodule.presentation.presenters.impl;

import com.afra55.apimodule.domain.interactors.LoginInteractor;
import com.afra55.apimodule.domain.interactors.impl.LoginInteractorImpl;
import com.afra55.apimodule.domain.model.LoginBean;
import com.afra55.apimodule.presentation.presenters.LoginPresenter;
import com.afra55.apimodule.storage.LoginRepositoryImpl;
import com.afra55.commontutils.base.AbstractPresenter;
import com.afra55.commontutils.base.Executor;
import com.afra55.commontutils.base.MainThread;

/**
 * Created by yangshuai on 2017/5/12.
 */

public class LoginPresenterImpl extends AbstractPresenter implements LoginPresenter, LoginInteractor.Callback{

    private LoginPresenter.View mView;
    private LoginInteractor loginInteractor;

    public LoginPresenterImpl(Executor executor, MainThread mainThread, LoginPresenter.View view) {
        super(executor, mainThread, view);
        this.mView = view;
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void destroy() {
        if (loginInteractor != null) {
            loginInteractor.cancel();
        }
    }

    @Override
    public void toLogin(long phone) {
        loginInteractor = new LoginInteractorImpl(
                mExecutor, mMainThread, new LoginRepositoryImpl(), phone, this);
        loginInteractor.execute();
    }

    @Override
    public void onLoginResultReturn(LoginBean loginBean) {
        mView.onLoginResultReturn(loginBean);
    }
}
