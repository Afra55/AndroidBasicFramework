package com.afra55.commontutils.http;

/**
 * Created by yangshuai on 2017/8/19.
 * {link http://afra55.github.io}
 */

public interface BasePresenter<T extends IActionListener.ViewAction> {

    /**
     * 添加view
     *
     * @param view
     */
    void addView(T view);

    /**
     * 移除view
     */
    void removeView();

}
