package com.study.boot1.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailAuth {

    private Long idx;
    private Integer userAuthIdx;
    private String token;
    private Date createdAt;
    private Date authAt;
}
