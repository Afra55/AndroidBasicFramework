package com.afra55.apimodule.presentation.presenters;

import com.afra55.apimodule.domain.model.TranslateBean;
import com.afra55.commontutils.base.BasePresenter;
import com.afra55.apimodule.presentation.ui.BaseView;

/**
 * Created by yangshuai on 2017/5/11.
 */

public interface ToTranslatePresenter extends BasePresenter {

    interface View extends BaseView{
        void onTranslateResultReturn(TranslateBean translateBean);
    }

    void translateText(String text);
}
