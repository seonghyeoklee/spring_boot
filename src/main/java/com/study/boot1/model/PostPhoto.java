package com.study.boot1.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostPhoto {
    private long idx;
    private long postIdx;
    private int order;
    private String path;
    private Date createdAt;
}