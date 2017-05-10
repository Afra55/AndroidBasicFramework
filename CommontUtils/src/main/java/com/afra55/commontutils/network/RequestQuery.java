package com.afra55.commontutils.network;

import java.util.HashMap;
import java.util.Map;

public class RequestQuery {

    private Map<String,Object> data = new HashMap<>();


    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }


    public static class Build{
        private Map<String,Object> buildData = new HashMap<>();

        public Build(){};

        public Build withParams(String params, Object obj){
            if (obj!=null){
                buildData.put(params,obj);
            }
            return this;
        }

        public Map<String,Object> build(){
            RequestQuery query = new RequestQuery();
            query.setData(buildData);
            return query.getData();
        }

    }

}
