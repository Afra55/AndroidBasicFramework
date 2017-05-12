package com.afra55.commontutils.base;

import com.alibaba.fastjson.JSON;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.io.Serializable;

/**
 * Created by Victor Yang on 2016/7/1.
 */
public class BaseBean extends BaseModel implements Serializable{

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

}
