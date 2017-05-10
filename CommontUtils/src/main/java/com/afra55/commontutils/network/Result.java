package com.afra55.commontutils.network;

/**
 * @author houwenpeng
 * @version V1.0
 * @Package yuanxiaobao
 * @Title com.hwp.base.net
 * @date 16/12/10
 * @Description:
 */
public class Result {

    private static final String HTTP_SERVICE_SUCCESS = "0000000";

    private String bizData;
    private String rtnCode;
    private String msg;
    private long ts;

    public String getBizData() {
        return bizData;
    }

    public void setBizData(String bizData) {
        this.bizData = bizData;
    }

    public String getRtnCode() {
        return rtnCode;
    }

    public void setRtnCode(String rtnCode) {
        this.rtnCode = rtnCode;
    }

    public long getTs() {
        return ts;
    }

    public void setTs(long ts) {
        this.ts = ts;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isSuccess(){
        if (HTTP_SERVICE_SUCCESS.endsWith(rtnCode)){
            return true;
        }
        return false;
    }
}