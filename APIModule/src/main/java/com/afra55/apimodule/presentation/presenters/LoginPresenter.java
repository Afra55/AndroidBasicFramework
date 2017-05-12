package com.afra55.apimodule.presentation.presenters;

import com.afra55.apimodule.domain.model.LoginBean;
import com.afra55.commontutils.base.BasePresenter;
import com.afra55.commontutils.base.BaseView;

/**
 * Created by yangshuai on 2017/5/12.
 */

public interface LoginPresenter extends BasePresenter {

    interface View extends BaseView {
        void onLoginResultReturn(LoginBean loginBean);
    }

    void toLogin(long phone);

}
