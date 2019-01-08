package com.study.boot1.service;

import com.study.boot1.dao.PostDAO;
import com.study.boot1.model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class PostServiceImpl implements PostService{
    @Autowired
    PostDAO postDAO;

    @Override
    public List<Post> selectPostList(Map<String, Object> map) {

        List<Post> list = postDAO.selectPostList(map);
        return list;
    }

    @Override
    public int insertPost(Post post) {

        return postDAO.insertPost(post);
    }
}
