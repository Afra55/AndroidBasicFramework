package com.afra55.commontutils.network;

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
