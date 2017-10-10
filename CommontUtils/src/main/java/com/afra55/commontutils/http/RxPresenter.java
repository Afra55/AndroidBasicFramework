package com.afra55.commontutils.http;

import android.view.ViewGroup;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;


/**
 * Created by yangshuai on 2017/8/19.
 * {link http://afra55.github.io}
 */

public class RxPresenter implements BasePresenter {

    protected IActionListener.ViewAction viewAction;
    private CompositeDisposable compositeSubscription;

    public RxPresenter(IActionListener.ViewAction view) {
        addView(view);
    }

    public IActionListener.ViewAction getBaseView() {
        return viewAction;
    }

    @Override
    public void addView(IActionListener.ViewAction view) {
        this.viewAction = view;
    }

    @Override
    public void removeView() {
        if (viewAction != null && (viewAction instanceof ViewGroup)) {
            ((ViewGroup) viewAction).removeAllViews();
            viewAction = null;
        }
        unSubscriber();
    }

    protected <T> DisposableObserver<T> addSubscriber(DisposableObserver<T> disposableObserver) {
        if (compositeSubscription == null) {
            compositeSubscription = new CompositeDisposable();
        }
        compositeSubscription.add(disposableObserver);
        return disposableObserver;
    }

    protected void unSubscriber() {
        if (compositeSubscription != null) {
            compositeSubscription.clear();
        }
    }


}