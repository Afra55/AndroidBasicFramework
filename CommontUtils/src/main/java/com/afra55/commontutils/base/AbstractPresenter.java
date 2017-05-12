package com.afra55.commontutils.base;


/**
 */
public abstract class AbstractPresenter {
    protected Executor mExecutor;
    protected MainThread mMainThread;
    private BaseView mView;

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
