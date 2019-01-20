package com.study.boot1.dao;

import com.study.boot1.model.Reply;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReplyDAO {
    List<Reply> selectReplyList(@Param("offset") long offset, @Param("count") int count);

    int insertReply(Reply reply);

    int deleteReply(@Param("replyIdx") long replyIdx, @Param("authorIdx") int authorIdx);

    int insertReplyLike(@Param("replyIdx") long replyIdx, @Param("userIdx") long userIdx);

    int deleteReplyLike(@Param("replyIdx") long replyIdx, @Param("userIdx") long userIdx);
}
