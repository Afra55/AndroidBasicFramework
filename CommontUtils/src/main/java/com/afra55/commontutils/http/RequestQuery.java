package com.afra55.commontutils.http;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yangshuai on 2017/8/19.
 * {link http://afra55.github.io}
 */

public class RequestQuery {

    public static RequestQuery.Build getBuildInstance() {
        return new RequestQuery.Build();
    }

    private Map<String,Object> data = new HashMap<>();

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }


    public static class Build{
        private Map<String,Object> buildData = new HashMap<>();

        public Build() {
        }

        public Build withParam(String param, Object obj) {
            if (obj!=null){
                buildData.put(param, obj);
            }
            return this;
        }

        public Map<String, Object> build() {
            RequestQuery query = new RequestQuery();
            query.setData(buildData);
            return query.getData();
        }

    }
}
