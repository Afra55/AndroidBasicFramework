package com.afra55.commontutils.network;

/**
 * @author houwenpeng
 * @version V1.0
 * @Package yuanxiaobao
 * @Title com.hwp.framework.network
 * @date 16/12/12
 * @Description:
 */
public class HttpRuntimeException extends RuntimeException {

    private String errorCode;
    private String errorMsg;



    public HttpRuntimeException(String errorCode, String errorMsg){
        super(errorMsg);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public String getErrorCode() {
        return errorCode;
    }


    public String getErrorMsg() {
        return errorMsg;
    }

}
