package com.afra55.baseclient.base.ui;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

/**
 * Created by Victor Yang on 2016/6/28.
 */
public interface MainActivityUI {

    void onCreate(Bundle savedInstanceState);

    void onSaveInstanceState(Bundle outState);

    void initBottomMenu(View view);

    void setCurrentBottomMenuView(int id);

    void showHomeFragment();

    void showCommunityFragment();

    void showShopFragment();

    void showMeFragment();

    void resetBottomMenuState();

    boolean onKeyDown(int keyCode, KeyEvent event);
}
