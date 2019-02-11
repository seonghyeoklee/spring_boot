package com.study.boot1.model;

import lombok.Data;

@Data
public class EmailAuth {

    private int userAuthIdx;
    private String token;
}
