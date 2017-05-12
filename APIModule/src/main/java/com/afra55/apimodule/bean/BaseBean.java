package com.afra55.apimodule.bean;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;

/**
 * Created by Victor Yang on 2016/7/1.
 */
public class BaseBean implements Serializable{

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

}
