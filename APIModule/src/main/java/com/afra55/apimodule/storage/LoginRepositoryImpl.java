package com.afra55.apimodule.storage;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.afra55.apimodule.domain.model.LoginBean;
import com.afra55.apimodule.domain.repository.LoginRepository;
import com.afra55.commontutils.base.BaseBean;
import com.raizlabs.android.dbflow.sql.language.SQLite;


/**
 * Created by yangshuai on 2017/5/12.
 */

public class LoginRepositoryImpl implements LoginRepository {

    @Override
    public void insert(@NonNull BaseBean data) {
        data.insert();
    }

    @Override
    public void update(@NonNull BaseBean data) {
        data.update();
    }

    @Override
    public void delete(@NonNull BaseBean data) {
        data.delete();
    }

    @Override
    public String getToken() {

        LoginBean loginBean = getLoginBean();
        if (loginBean != null) {
            return loginBean.getToken();
        } else {
            return "";
        }
    }

    @Nullable
    @Override
    public LoginBean getLoginBean() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return SQLite
                .select()
                .from(LoginBean.class)
                .querySingle();
    }
}
