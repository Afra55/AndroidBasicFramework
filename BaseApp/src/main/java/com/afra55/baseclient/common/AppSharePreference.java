package com.afra55.baseclient.common;


import com.afra55.commontutils.storage.BaseSharePreference;

/**
 * Created by Afra55 on 2017/10/19.
 * Smile is the best name card.
 */

public class AppSharePreference extends BaseSharePreference {

    private static AppSharePreference instance = null;

    public static AppSharePreference getInstance() {
        if (instance == null) {
            synchronized (AppSharePreference.class) {
                instance = new AppSharePreference();
            }
        }
        return instance;
    }

    private AppSharePreference() {
        super();
    }


}
