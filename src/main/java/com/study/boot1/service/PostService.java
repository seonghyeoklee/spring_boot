package com.study.boot1.service;

import com.study.boot1.model.Post;

public interface PostService {
    Object getPostList(Integer offset, Integer count);

    Object createPost(Post post);

    Object updatePost(Post post);

    Object deletePost(long postIdx, int userIdx);

    Object likePost(int userIdx, long postIdx);

    Object unlikePost(int userIdx, long postIdx);
}
