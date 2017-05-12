package com.afra55.apimodule.domain.interactors;

import com.afra55.apimodule.domain.model.LoginBean;
import com.afra55.commontutils.base.BaseCallback;
import com.afra55.commontutils.base.Interactor;

/**
 * Created by yangshuai on 2017/5/12.
 */

public interface LoginInteractor extends Interactor {
    interface Callback extends BaseCallback {
        void onLoginResultReturn(LoginBean loginBean);
    }

}
