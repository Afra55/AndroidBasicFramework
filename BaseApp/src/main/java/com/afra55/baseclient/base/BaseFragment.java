package com.afra55.baseclient.base;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.View;

import com.afra55.baseclient.base.presenter.BaseFragmentPresenter;
import com.afra55.baseclient.base.ui.BaseFragmentUI;

public abstract class BaseFragment extends Fragment implements View.OnClickListener, BaseFragmentUI {

    private BaseFragmentPresenter mBaseFragmentPresenter;

    protected final boolean isDestroyed() {
        return mBaseFragmentPresenter.isDestroyed();
    }

    public int getContainerId() {
        return mBaseFragmentPresenter.getContainerId();
    }

    public void setContainerId(int containerId) {
        mBaseFragmentPresenter.setContainerId(containerId);
    }

    public BaseFragment() {
        mBaseFragmentPresenter = new BaseFragmentPresenter(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBaseFragmentPresenter.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mBaseFragmentPresenter.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mBaseFragmentPresenter.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mBaseFragmentPresenter.onDetach();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mBaseFragmentPresenter.onDestroy();
    }

    @Override
    public final Handler getHandler() {
        return mBaseFragmentPresenter.getHandler();
    }

    @Override
    public final void postRunnable(final Runnable runnable) {
       mBaseFragmentPresenter.postRunnable(runnable);
    }

    @Override
    public final void postDelayed(final Runnable runnable, long delay) {
       mBaseFragmentPresenter.postDelayed(runnable, delay);
    }

    /**
     * 当Fragment选中时
     */
    @Override
    public void setFragmentSeleted(boolean selected) {
        mBaseFragmentPresenter.setFragmentSeleted(selected);
    }

    /**
     * 在onActivityCreated(Bundle savedInstanceState)里使用
     *
     * @param resId
     * @param <T>
     * @return
     */
    @Override
    public <T extends View> T findView(int resId) {
        return mBaseFragmentPresenter.findView(resId);
    }

    @Override
    public void showKeyboard(boolean isShow) {
        mBaseFragmentPresenter.showKeyboard(isShow);
    }

    @Override
    public void hideKeyboard(View view) {
        mBaseFragmentPresenter.hideKeyboard(view);
    }

    @Override
    public String getInitParam1() {
        return mBaseFragmentPresenter.getInitParam1();
    }

    @Override
    public String getInitParam2() {
        return mBaseFragmentPresenter.getInitParam2();
    }
}
