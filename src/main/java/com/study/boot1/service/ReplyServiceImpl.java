package com.study.boot1.service;

import com.study.boot1.common.Constant;
import com.study.boot1.dao.ReplyDAO;
import com.study.boot1.exception.BadRequestException;
import com.study.boot1.exception.InternalServerErrorException;
import com.study.boot1.model.Reply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReplyServiceImpl implements ReplyService {

    @Autowired
    ReplyDAO replyDAO;

    @Override
    public Object getReplyList(Long offset, Integer count) {
        offset = Math.max(offset, 0);
        count = Math.max(count, 0);

        if(count > Constant.REPLY_LIST_MAX_COUNT)
            throw new BadRequestException(0, "");

        List<Reply> replies = replyDAO.selectReplyList(offset, count);

        Map<String, Object> result = new HashMap<>();
        result.put("replies", replies);
        return result;
    }

    @Override
    public Object createReply(Reply reply) {
        int insertCount = replyDAO.insertReply(reply);

        if(insertCount == 0)
            throw new InternalServerErrorException(0, "");

        Map<String, Object> result = new HashMap<>();
        result.put("replyIdx", reply.getIdx());
        return result;
    }

    @Override
    public Object updateReply(Reply reply) {
        return null;
    }

    @Override
    public Object deleteReply(int userIdx, long replyIdx) {
        int deleteCount = replyDAO.deleteReply(replyIdx, userIdx);

        if(deleteCount == 0)
            throw new InternalServerErrorException(0, "");

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public Object likeReply(int userIdx, long replyIdx) {
        replyDAO.insertReplyLike(replyIdx, userIdx);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public Object unlikeReply(int userIdx, long replyIdx) {
        replyDAO.deleteReplyLike(replyIdx, userIdx);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
