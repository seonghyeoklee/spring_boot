package com.study.boot1.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Post {

    private long idx;
    private User author;
    private String title;
    private String content;
    private int state;
    private Date createdAt;
    private Date updatedAt;

    private int likeCount;
}
