package com.afra55.commontutils.http;

import android.view.ViewGroup;

import rx.Subscriber;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by yangshuai on 2017/8/19.
 * {link http://afra55.github.io}
 */

public class RxPresenter implements BasePresenter {

    private IActionListener.ViewAction view;
    private CompositeSubscription compositeSubscription;

    public RxPresenter(IActionListener.ViewAction view) {
        addView(view);
    }

    public IActionListener.ViewAction getBaseView() {
        return view;
    }

    @Override
    public void addView(IActionListener.ViewAction view) {
        this.view = view;
    }

    @Override
    public void removeView() {
        if (view != null && (view instanceof ViewGroup)) {
            ((ViewGroup) view).removeAllViews();
            view = null;
        }
        unSubscriber();
    }

    protected <T> Subscriber addSubscriber(Subscriber<T> subscriber) {
        if (compositeSubscription == null) {
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