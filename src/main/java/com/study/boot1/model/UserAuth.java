package com.study.boot1.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAuth {
    private int idx;
    private int userIdx;
    private User user;
    private int type;
    private String identification;
    private String credential;
    private Date createdAt;
}
