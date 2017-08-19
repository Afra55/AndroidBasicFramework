package com.afra55.commontutils.http;

import android.view.ViewGroup;

import rx.Subscriber;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by yangshuai on 2017/8/19.
 * {link http://afra55.github.io}
 */

public class RxPresenter implements BasePresenter {

    protected IActionListener.ViewAction viewAction;
    private CompositeSubscription compositeSubscription;

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

    protected <T> Subscriber<T> addSubscriber(Subscriber<T> subscriber){
        if(compositeSubscription == null){
            compositeSubscription = new CompositeSubscription();
        }
        compositeSubscription.add(subscriber);
        return subscriber;
    }

    protected void unSubscriber() {
        if (compositeSubscription != null) {
            compositeSubscription.unsubscribe();
        }
    }


}