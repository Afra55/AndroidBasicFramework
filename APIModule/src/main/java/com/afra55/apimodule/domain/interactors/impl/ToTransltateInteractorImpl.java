package com.afra55.apimodule.domain.interactors.impl;

import com.afra55.apimodule.domain.executor.Executor;
import com.afra55.apimodule.domain.executor.MainThread;
import com.afra55.apimodule.domain.interactors.ToTranslateInteractor;
import com.afra55.apimodule.domain.interactors.base.AbstractInteractor;

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

    }
}
