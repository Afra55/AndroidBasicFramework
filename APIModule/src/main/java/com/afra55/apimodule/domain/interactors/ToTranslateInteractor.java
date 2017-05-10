package com.afra55.apimodule.domain.interactors;

import com.afra55.apimodule.domain.model.TranslateBean;

/**
 * Created by yangshuai on 2017/5/10.
 */

public interface ToTranslateInteractor {


    interface Callback {
        void getTranslateResult(TranslateBean translateBean);
    }
}
