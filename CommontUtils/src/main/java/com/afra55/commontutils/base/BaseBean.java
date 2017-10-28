package com.afra55.commontutils.base;

import java.io.Serializable;

/**
 * Created by Afra55 on 2017/10/9.
 * Smile is the best name card.
 * 所有的 bean 文件需要继承 BaseBean
 */

public class BaseBean implements Serializable {

    private int viewType = BaseFlag.DefaultData.NONE;

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }
}
