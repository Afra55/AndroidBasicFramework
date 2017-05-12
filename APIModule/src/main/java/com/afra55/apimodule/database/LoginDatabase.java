package com.afra55.apimodule.database;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Created by yangshuai on 2017/5/12.
 */
@Database(name = LoginDatabase.NAME, version = LoginDatabase.VERSION)
public class LoginDatabase {
    public static final String NAME = "Login";

    public static final int VERSION = 1;
}
