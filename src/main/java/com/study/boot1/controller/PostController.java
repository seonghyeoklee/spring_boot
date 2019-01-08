package com.study.boot1.controller;

import com.google.gson.internal.LinkedTreeMap;
import com.study.boot1.common.Constant;
import com.study.boot1.model.Post;
import com.study.boot1.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(Constant.REST_API_VERSION + "/post")
@Slf4j
public class PostController {

    @Autowired
    PostService postService;

    @GetMapping("/list")
    public Object listGET(
            @RequestParam(value="offset", required = false) Integer offset,
            @RequestParam(value="count", required = false) Integer count){

        Map<String, Object> params = new HashMap<>();
        params.put("offset", offset);
        params.put("count", count);

        List<Post> list = postService.selectPostList(params);

        Map<String, Object> result = new LinkedTreeMap<>();
        result.put("list", list);

        return result;
    }

    @PostMapping("")
    public Object createPOST(Post post){

        log.info("author_idx : " + post.getAuthor().getIdx());
        log.info("title : " + post.getTitle());
        log.info("content : " + post.getContent());

        boolean succ = false;
        int insertCount = postService.insertPost(post);

        log.info("insertCount : " + insertCount);
        log.info("insertIdx : " + post.getIdx());

        if(insertCount == 1) {
            succ = true;
        }

        Map<String, Object> result = new LinkedTreeMap<>();
        result.put("succ", succ);
        result.put("insertCount", insertCount);

        return result;
    }

    @GetMapping("/{idx}")
    public Object readGET(@PathVariable int idx){

        log.info("idx : " + idx);

        boolean succ = false;
        Post post = postService.selectPost(idx);

        if(post != null) {
            succ = true;
        }

        Map<String, Object> result = new LinkedTreeMap<>();
        result.put("succ", succ);
        result.put("post", post);

        return result;
    }

    @PutMapping("/{idx}")
    public Object updatePUT(@PathVariable int idx, Post post){

        log.info("idx : " + idx);

        boolean succ = false;

        post.setIdx(idx);
        int updateCount = postService.updatePost(post);

        if(updateCount == 1) {
            succ = true;
        }

        Map<String, Object> result = new LinkedTreeMap<>();
        result.put("succ", succ);
        result.put("post", updateCount);

        return result;
    }

    @DeleteMapping("/{idx}")
    public Object deleteDELETE(@PathVariable int idx){

        log.info("idx : " + idx);

        boolean succ = false;
        int deleteCount = postService.deletePost(idx);

        if(deleteCount == 1) {
            succ = true;
        }

        Map<String, Object> result = new LinkedTreeMap<>();
        result.put("succ", succ);
        result.put("deleteCount", deleteCount);

        return result;
    }

    @PostMapping("/{idx}/like")
    public Object likePOST(@PathVariable int idx){

        log.info("idx : " + idx);

        int postLikeCount = postService.selectPostLike(idx);

        Map<String, Object> result = new LinkedTreeMap<>();
        result.put("succ", true);
        result.put("postLikeCount", postLikeCount);

        return result;
    }

    @PostMapping("/{idx}/unlike")
    public Object unlikePOST(){

        return null;
    }
}
