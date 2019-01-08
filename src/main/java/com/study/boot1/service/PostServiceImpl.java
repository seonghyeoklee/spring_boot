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

        return postDAO.selectPostList(map);
    }

    @Override
    public int insertPost(Post post) {

        return postDAO.insertPost(post);
    }

    @Override
    public Post selectPost(int idx) {

        return postDAO.selectPost(idx);
    }

    @Override
    public int updatePost(Post post) {

        return postDAO.updatePost(post);
    }

    @Override
    public int deletePost(int idx) {

        return postDAO.deletePost(idx);
    }

    @Override
    public int selectPostLike(int idx) {

        return postDAO.selectPostLike(idx);
    }
}
