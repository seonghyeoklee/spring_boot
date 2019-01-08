package com.study.boot1.dao;

import com.study.boot1.model.Post;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface PostDAO {

    List<Post> selectPostList(Map<String, Object> map);

    int insertPost(Post post);
}
