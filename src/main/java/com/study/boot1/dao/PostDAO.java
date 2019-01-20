package com.study.boot1.dao;

import com.study.boot1.model.Post;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostDAO {
    List<Post> selectPostList(@Param("offset") long offset, @Param("limit") int limit);

    int insertPost(Post post);

    int updatePost(Post post);

    int deletePost(@Param("postIdx") long postIdx, @Param("authorIdx") int authorIdx);

    int insertPostLike(@Param("postIdx") long postIdx, @Param("userIdx") int userIdx);

    int deletePostLike(@Param("postIdx") long postIdx, @Param("userIdx") int userIdx);
}
