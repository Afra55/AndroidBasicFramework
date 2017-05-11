package com.afra55.apimodule.presentation.presenters.impl;

import com.afra55.apimodule.domain.interactors.ToTranslateInteractor;
import com.afra55.apimodule.domain.interactors.impl.ToTranslateInteractorImpl;
import com.afra55.apimodule.domain.model.TranslateBean;
import com.afra55.apimodule.presentation.presenters.CommunityPresenter;
import com.afra55.commontutils.base.AbstractPresenter;
import com.afra55.commontutils.base.Executor;
import com.afra55.commontutils.base.MainThread;

/**
 * Created by yangshuai on 2017/5/11.
 */

public class CommunityPresenterImpl extends AbstractPresenter
        implements CommunityPresenter, ToTranslateInteractor.Callback{

    private CommunityPresenter.View mView;

    public CommunityPresenterImpl(Executor executor, MainThread mainThread, View view) {
        super(executor, mainThread);
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

    }

    @Override
    public void onError(String message) {
        mView.showError(message);
    }

    @Override
    public void translateText(String text) {
        ToTranslateInteractor toTranslateInteractor = new ToTranslateInteractorImpl(
                mExecutor, mMainThread, text, this);
        toTranslateInteractor.execute();
    }

    @Override
    public void onTranslateResultReturn(TranslateBean translateBean) {
        mView.onTranslateResultReturn(translateBean);
    }

    @Override
    public void onStart() {
        mView.showProgress();
    }

    @Override
    public void onCompleted() {
        mView.hideProgress();
    }
}
