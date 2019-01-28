package com.study.boot1.controller;

import com.study.boot1.SwaggerResponseErrorCodes;
import com.study.boot1.common.Constant;
import com.study.boot1.common.ErrorCode;
import com.study.boot1.model.Post;
import com.study.boot1.model.User;
import com.study.boot1.resolver.SessionLogin;
import com.study.boot1.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Constant.REST_API_VERSION + "/post")
@Slf4j
public class PostController {

    @Autowired
    PostService postService;

    @SwaggerResponseErrorCodes({
            ErrorCode.INVALID_PARAM_ACCOUNT_TYPE, ErrorCode.INVALID_QUERY_EXCEPTION
    })

    @GetMapping("/list")
    public Object listGET(
            @RequestParam(value="offset", required = false) Long offset,
            @RequestParam(value="count", required = false) Integer count){

        if(offset ==null){
            offset = Constant.POST_LIST_DEFAULT_OFFSET;
        }

        if(count ==null){
            count = Constant.POST_LIST_DEFAULT_COUNT;
        }

        return postService.getPostList(offset, count);
    }

    @PostMapping("")
    public Object createPOST(@SessionLogin Integer userIdx, @RequestBody Post post){
        User author = new User();
        author.setIdx(userIdx);
        post.setAuthor(author);

        return postService.createPost(post);
    }

    @GetMapping("/{idx}")
    public Object readGET(@PathVariable("idx") Integer postIdx){

        return null;
    }

    @PutMapping("/{idx}")
    public Object updatePUT(@SessionLogin Integer userIdx, @PathVariable("idx") Integer postIdx, @RequestBody Post post){

        return postService.updatePost(post);
    }

    @DeleteMapping("/{idx}")
    public Object deleteDELETE( @SessionLogin Integer userIdx, @PathVariable("idx") Integer postIdx ) {

        return postService.deletePost(userIdx, postIdx);
    }

    @PostMapping("/{idx}/like")
    public Object likePOST( @SessionLogin Integer userIdx, @PathVariable("idx") Integer postIdx ){

        return postService.likePost(userIdx, postIdx);
    }

    @PostMapping("/{idx}/unlike")
    public Object unlikePOST( @SessionLogin Integer userIdx, @PathVariable("idx") Integer postIdx ){

        return postService.unlikePost(userIdx, postIdx);
    }
}
