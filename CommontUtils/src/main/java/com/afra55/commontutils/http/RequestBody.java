package com.afra55.commontutils.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.Hashtable;
import java.util.Map;

/**
 * Created by yangshuai on 2017/8/19.
 * {link http://afra55.github.io}
 */

public class RequestBody {

    public static RequestBody.Builder getBuilderInstance() {
        return new RequestBody.Builder();
    }

    private String style;
    private Map<String, Object> data = new Hashtable<>();

    public void setStyle(String style) {
        this.style = style;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }


    public String getStyle() {
        return style;
    }

    public Map<String, Object> getData() {
        return data;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    public static class Builder {
        private String style = "black";
        private Map<String, Object> data = new Hashtable<>();

        public Builder() {}

        public Builder withStyle(String style) {
            this.style = style;
            return this;
        }

        public Builder withData(Map<String, Object> data) {
            if(data != null){
                this.data = data;
            }
            return this;
        }

        public Builder withParam(String key, Object value) {
            if(value != null){
                this.data.put(key, value);
            }
            return this;
        }

        /**
         * .withObject(JSON.toJSONString(bean))
         * @param jsonStr JSON.toJSONString(bean)
         * @return this
         */
        public Builder withObject(String jsonStr){
            if (jsonStr != null){
                JSONObject jasonObject = JSONObject.parseObject(jsonStr);
                @SuppressWarnings("unchecked")
                Map<String, Object> map = (Map) jasonObject;
                for (Map.Entry<String, Object> entry : map.entrySet()){
                    this.data.put(entry.getKey(),entry.getValue());
                }
            }
            return this;
        }


        public RequestBody build() {
            RequestBody request = new RequestBody();
            request.setStyle(this.style);
            request.setData(this.data);
            return request;
        }

    }

}
