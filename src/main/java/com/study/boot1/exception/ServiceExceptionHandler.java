package com.study.boot1.exception;

import com.google.gson.internal.LinkedTreeMap;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;

@ControllerAdvice
public class ServiceExceptionHandler {

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Object handle(BadRequestException e) {
        Map<String, Object> result = new LinkedTreeMap<>();
        result.put("code", e.getCode());
        result.put("msg", e.getMsg());
        return result;
    }
}
