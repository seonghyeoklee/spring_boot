package com.study.boot1;

import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

@Configuration
public class ErrorConfig {
    @Bean
    public ErrorAttributes errorAttributes(){
        return new DefaultErrorAttributes(){
            @Override
            public Map<String, Object> getErrorAttributes(WebRequest webRequest, boolean includeStackTrace) {
                Map<String, Object> attrs = super.getErrorAttributes(webRequest, includeStackTrace);

                attrs.put("code", attrs.get("status"));

                attrs.remove("timestamp");
                attrs.remove("status");
                attrs.remove("error");
                attrs.remove("path");

                return attrs;
            }
        };
    }

}

/*
{
        "timestamp": "2019-01-12T05:28:46.076+0000",
        "status": 400,
        "error": "Bad Request",
        "message": "Required int parameter 'type' is not present",
        "path": "/v1/sign/in"
        }*/
