package com.afra55.commontutils.base;


import com.afra55.commontutils.threading.MainThreadImpl;
import com.afra55.commontutils.threading.ThreadExecutor;

/**
 */
public abstract class AbstractPresenter {
    protected Executor mExecutor;
    protected MainThread mMainThread;
    private BaseView mView;

    public AbstractPresenter(BaseView view) {
        this(ThreadExecutor.getInstance()
                , MainThreadImpl.getInstance()
                , view);
    }

    public AbstractPresenter(Executor executor, MainThread mainThread, BaseView view) {
        mExecutor = executor;
        mMainThread = mainThread;
        mView = view;
    }

    public void onStart() {
        mView.showProgress();
    }

    public void onCompleted() {
        mView.hideProgress();
    }

    public void onError(String message) {
        mView.showError(message);
    }
}
