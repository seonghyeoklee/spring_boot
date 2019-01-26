package com.study.boot1.common;

import org.springframework.http.HttpStatus;

public enum  ErrorCode {

    INVALID_PARAM_ACCOUNT_TYPE(HttpStatus.BAD_REQUEST, 10001, "INVALID_PARAM_ACOUNT_TYPE"),

    EXCEED_POST_LIST_MAX_COUNT(HttpStatus.BAD_REQUEST, 20001, "EXCEED_POST_LIST_MAX_COUNT"),

    INVALID_QUERY_EXCEPTION(HttpStatus.BAD_REQUEST, 30001, "INVALID_QUERY_EXCEPTION"),

    UNKNOWN(HttpStatus.BAD_REQUEST, 50000, "UNKNOWN")
    ;

    private final HttpStatus httpStatus;
    private final int code;
    private final String msg;

    ErrorCode (HttpStatus httpStatus, int code, String msg){
        this.httpStatus = httpStatus;
        this.code = code;
        this.msg = msg;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
