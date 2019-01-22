package com.study.boot1.controller;

import com.study.boot1.common.Constant;
import com.study.boot1.model.Reply;
import com.study.boot1.resolver.SessionLogin;
import com.study.boot1.service.ReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Constant.REST_API_VERSION + "/reply")
public class ReplyController {

    @Autowired
    ReplyService replyService;

    @GetMapping("/list")
    public Object listGET(
            @RequestParam(value="offset", required = false) Long offset,
            @RequestParam(value="count", required = false) Integer count){

        if(offset ==null){
            offset = Constant.REPLY_LIST_DEFAULT_OFFSET;
        }

        if(count ==null){
            count = Constant.REPLY_LIST_DEFAULT_COUNT;
        }

        return replyService.getReplyList(offset, count);
    }

    @PostMapping("")
    public Object createREPLY(@SessionLogin Integer userIdx, @RequestBody Reply reply){

        return replyService.createReply(reply);
    }

    @GetMapping("/{idx}")
    public Object readGET(@PathVariable("idx") Integer replyIdx){

        return null;
    }

    @PutMapping("/{idx}")
    public Object updatePUT(@SessionLogin Integer userIdx, @PathVariable("idx") Integer replyIdx, @RequestBody Reply reply){

        return replyService.updateReply(reply);
    }

    @DeleteMapping("/{idx}")
    public Object deleteDELETE(@SessionLogin Integer userIdx, @PathVariable("idx") Integer replyIdx) {

        return replyService.deleteReply(userIdx, replyIdx);
    }

    @PostMapping("/{idx}/like")
    public Object likeREPLY(@SessionLogin Integer userIdx, @PathVariable("idx") Integer replyIdx){

        return replyService.likeReply(userIdx, replyIdx);
    }

    @PostMapping("/{idx}/unlike")
    public Object unlikeREPLY(@SessionLogin Integer userIdx, @PathVariable("idx") Integer replyIdx){

        return replyService.unlikeReply(userIdx, replyIdx);
    }
}
