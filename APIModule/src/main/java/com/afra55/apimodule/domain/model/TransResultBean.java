package com.afra55.apimodule.domain.model;

import com.afra55.apimodule.bean.BaseBean;

/**
 * Created by Victor Yang on 2016/9/19.
 */
public class TransResultBean extends BaseBean {
    private String src;
    private String dst;

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getDst() {
        return dst;
    }

    public void setDst(String dst) {
        this.dst = dst;
    }
}
