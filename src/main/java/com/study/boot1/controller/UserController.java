package com.study.boot1.controller;

import com.google.gson.internal.LinkedTreeMap;
import com.study.boot1.common.Constant;
import com.study.boot1.resolver.SessionLogin;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping(Constant.REST_API_VERSION+"/user")
public class UserController {
    @GetMapping("/my/info")
    public Object myInfoGET(@SessionLogin Integer userIdx) {
        Map<String, Object> result = new LinkedTreeMap<>();
        result.put("succ", true);
        result.put("user_idx", userIdx);
        return result;
    }
}
