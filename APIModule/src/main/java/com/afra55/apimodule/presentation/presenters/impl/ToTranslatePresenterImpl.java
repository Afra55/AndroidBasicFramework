package com.afra55.apimodule.presentation.presenters.impl;

import com.afra55.commontutils.base.Executor;
import com.afra55.commontutils.base.MainThread;
import com.afra55.apimodule.domain.interactors.ToTranslateInteractor;
import com.afra55.apimodule.domain.interactors.impl.ToTranslateInteractorImpl;
import com.afra55.apimodule.domain.model.TranslateBean;
import com.afra55.apimodule.presentation.presenters.ToTranslatePresenter;
import com.afra55.commontutils.base.AbstractPresenter;

/**
 * Created by yangshuai on 2017/5/11.
 */

public class ToTranslatePresenterImpl extends AbstractPresenter
        implements ToTranslatePresenter, ToTranslateInteractor.Callback{

    private ToTranslatePresenter.View mView;

    public ToTranslatePresenterImpl(Executor executor, MainThread mainThread, View view) {
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
}