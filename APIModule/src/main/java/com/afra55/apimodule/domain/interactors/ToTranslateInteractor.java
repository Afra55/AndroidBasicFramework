package com.afra55.apimodule.domain.interactors;

import com.afra55.apimodule.domain.model.TranslateBean;
import com.afra55.commontutils.base.BaseCallback;
import com.afra55.commontutils.base.Interactor;

/**
 * Created by yangshuai on 2017/5/10.
 */

public interface ToTranslateInteractor extends Interactor {


    interface Callback extends BaseCallback{
        void onTranslateResultReturn(TranslateBean translateBean);

    }
}
