package com.study.boot1.service;

import com.study.boot1.model.Post;

import java.util.List;
import java.util.Map;

public interface PostService {

    List<Post> selectPostList(Map<String, Object> map);

    int insertPost(Post post);
}
