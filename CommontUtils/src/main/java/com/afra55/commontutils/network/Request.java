package com.afra55.commontutils.network;

import com.afra55.commontutils.AppCache;
import com.afra55.commontutils.sys.AppInfoUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.Hashtable;
import java.util.Map;

public class Request {

    private AppInfoUtil.AppInfo AppInfo;
    private String style;
    private Map<String, Object> data = new Hashtable<>();

    public void setAppInfo(AppInfoUtil.AppInfo AppInfo) {
        this.AppInfo = AppInfo;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }


    public AppInfoUtil.AppInfo getAppInfo() {
        return AppInfo;
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
        private AppInfoUtil.AppInfo appInfo;
        private String style = "black";
        private Map<String, Object> data = new Hashtable<>();

        public Builder() {
            appInfo = AppInfoUtil.getAppInfo(AppCache.getContext());
        }

        public Builder withAppInfo(AppInfoUtil.AppInfo AppInfo) {
            this.appInfo = AppInfo;
            return this;
        }

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

        public Builder withObject(String jsonStr){
            if (jsonStr != null){
                JSONObject  jasonObject = JSONObject.parseObject(jsonStr);
                Map<String,String> map = (Map) jasonObject;
                for (Map.Entry<String,String> entry : map.entrySet()){
                    this.data.put(entry.getKey(),entry.getValue());
                }
            }
            return this;
        }


        public Request build() {
            Request request = new Request();
            request.setStyle(this.style);
            request.setAppInfo(this.appInfo);
            request.setData(this.data);
            return request;
        }

    }
}

