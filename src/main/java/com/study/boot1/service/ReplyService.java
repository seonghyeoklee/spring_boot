package com.study.boot1.service;

import com.study.boot1.model.Reply;

public interface ReplyService {

    Object getReplyList(Long offset, Integer count);

    Object createReply(Reply reply);

    Object updateReply(Reply reply);

    Object deleteReply(int userIdx, long replyIdx);

    Object likeReply(int userIdx, long replyIdx);

    Object unlikeReply(int userIdx, long replyIdx);
}
