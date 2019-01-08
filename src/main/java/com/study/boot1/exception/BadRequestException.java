package com.study.boot1.exception;

import com.study.boot1.common.ErrorCode;

public class BadRequestException extends RuntimeException{

    private int code;
    private String msg;

    public BadRequestException(int code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public BadRequestException(ErrorCode errorCode){
        this.code = errorCode.getCode();
        this.msg = errorCode.getMsg();
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
