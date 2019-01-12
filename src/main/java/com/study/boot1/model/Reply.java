package com.study.boot1.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reply {
    private long idx;
    private long postIdx;
    private User author;
    private String content;
    private int state;
    private Date createAt;
    private Date updateAt;
}
