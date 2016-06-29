package com.afra55.commontutils.base.ui;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.afra55.commontutils.base.BaseFragment;

/**
 * Created by Victor Yang on 2016/6/25.
 * MVP- V
 */
public interface BaseFragmentUI {

    void onActivityCreated(Bundle savedInstanceState);

    Handler getHandler();

    void postRunnable(final Runnable runnable);

    void postDelayed(final Runnable runnable, long delay);

    void setFragmentSeleted(boolean selected);

    void showKeyboard(boolean isShow);

    void hideKeyboard(View view);

    Context getContext();

    BaseFragment getFragment();

    <T extends View> T findView(int resId);

    String getInitParam1();
    String getInitParam2();
}
