package com.afra55.baseclient.base.ui;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

/**
 * Created by Victor Yang on 2016/6/25.
 * MVP- V
 */
public interface BaseFragmentUI {

    void onButtonPressed(Uri uri);

    void onActivityCreated(Bundle savedInstanceState);

    Handler getHandler();

    void postRunnable(final Runnable runnable);

    void postDelayed(final Runnable runnable, long delay);

    void setFragmentSeleted(boolean selected);

    void showKeyboard(boolean isShow);

    void hideKeyboard(View view);

    Activity getActivity();

    Context getContext();


}
