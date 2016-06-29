package com.afra55.commontutils.base.presenter;

import android.os.Handler;
import android.view.View;

import com.afra55.commontutils.base.ui.BaseFragmentUI;

/**
 * Created by Victor Yang on 2016/6/28.
 */
public class BaseFragmentPresenter {

    private BaseFragmentUI mBaseFragmentUI;

    public BaseFragmentPresenter(BaseFragmentUI baseFragmentUI) {
        mBaseFragmentUI = baseFragmentUI;
    }

}
