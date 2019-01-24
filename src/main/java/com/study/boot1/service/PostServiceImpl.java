package com.study.boot1.service;

import com.study.boot1.common.Constant;
import com.study.boot1.common.ErrorCode;
import com.study.boot1.dao.PostDAO;
import com.study.boot1.exception.InternalServerErrorException;
import com.study.boot1.model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PostServiceImpl implements PostService{
    @Autowired
    PostDAO postDAO;

    @Override
    public Object getPostList(Long offset, Integer count) {
        offset  = Math.max(offset, 0);
        count = Math.max(count, 0);

        if(count > Constant.POST_LIST_DEFAULT_COUNT){
            throw new InternalServerErrorException(ErrorCode.EXCEED_POST_LIST_MAX_COUNT);
        }

        List<Post> posts = postDAO.selectPostList(offset, count);

        Map<String, Object> result = new HashMap<>();
        result.put("posts", posts);

        return result;
    }

    @Override
    public Object createPost(Post post) {

        int insertCount = postDAO.insertPost(post);

        if(insertCount == 0){
            throw new InternalServerErrorException(ErrorCode.INVALID_QUERY_EXCEPTION);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("postIdx", post.getIdx());

        return result;
    }

    @Override
    public Object updatePost(Post post) {

        return postDAO.updatePost(post);
    }

    @Override
    public Object deletePost(long postIdx, int userIdx) {

        int deleteCount = postDAO.deletePost(postIdx, userIdx);

        if(deleteCount == 0)
            throw new InternalServerErrorException(ErrorCode.INVALID_QUERY_EXCEPTION);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public Object likePost(int userIdx, long postIdx) {

        postDAO.insertPostLike(postIdx, userIdx);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public Object unlikePost(int userIdx, long postIdx) {

        postDAO.deletePostLike(postIdx, userIdx);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
