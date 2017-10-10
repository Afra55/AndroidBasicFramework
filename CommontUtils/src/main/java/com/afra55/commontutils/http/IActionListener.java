package com.afra55.commontutils.http;

import com.afra55.commontutils.base.BaseView;

/**
 * Created by yangshuai on 2017/8/19.
 * {link http://afra55.github.io}
 */

public class IActionListener {
    /**
     * viewAction 接口的监听
     */
    public interface ViewAction<T> extends BaseView {
        void showInfoView(int type, Object obj);
    }

    /**
     * presenter 接口的监听
     */
    interface PresenterAciton extends BasePresenter {

    }
}
