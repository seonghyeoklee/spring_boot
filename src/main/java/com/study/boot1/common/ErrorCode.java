package com.study.boot1.common;

public enum  ErrorCode {

    INVALID_PARAM_ACOUNT_TYPE(10001, "INVALID_PARAM_ACOUNT_TYPE"),

    UNKNOWN(50000,"UNKNOWN");

    private final int code;
    private final String msg;

    ErrorCode (int code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
