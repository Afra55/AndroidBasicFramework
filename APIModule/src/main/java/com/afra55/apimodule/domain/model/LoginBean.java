package com.afra55.apimodule.domain.model;

import com.afra55.apimodule.database.LoginDatabase;
import com.afra55.commontutils.base.BaseBean;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;

/**
 * Created by yangshuai on 2017/5/12.
 */
@Table(database = LoginDatabase.class)
public class LoginBean extends BaseBean {
    @PrimaryKey
    private long id; // our base model already has an id, let's use it as a primary key

    @Column
    private String name;

    @Column
    private String token;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
