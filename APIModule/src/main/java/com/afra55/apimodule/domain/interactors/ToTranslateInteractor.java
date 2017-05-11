package com.afra55.apimodule.domain.interactors;

import com.afra55.commontutils.base.Interactor;
import com.afra55.apimodule.domain.model.TranslateBean;

/**
 * Created by yangshuai on 2017/5/10.
 */

public interface ToTranslateInteractor extends Interactor {


    interface Callback {
        void onTranslateResultReturn(TranslateBean translateBean);
    }
}
