package com.afra55.apimodule.domain.repository;

import com.afra55.apimodule.domain.model.LoginBean;
import com.afra55.commontutils.base.BaseRepository;

/**
 * Created by yangshuai on 2017/5/12.
 */

public interface LoginRepository extends BaseRepository {
    String getToken();

    LoginBean getLoginBean();
}
