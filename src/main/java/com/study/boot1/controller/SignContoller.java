package com.study.boot1.controller;

import com.google.gson.internal.LinkedTreeMap;
import com.study.boot1.common.Constant;
import com.study.boot1.model.User;
import com.study.boot1.model.UserAuth;
import com.study.boot1.service.SignService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping(Constant.REST_API_VERSION + "/sign")
@Slf4j
public class SignContoller {

    @Autowired
    SignService signService;

    //이메일 패스워드 로그인
    //소셜 로그인 가입/로그인
    @PostMapping("/in")
    public Object inPOST(
            HttpSession session,
            int type,
            String identification,
            String credential
    ){

        UserAuth userAuth = new UserAuth();
        userAuth.setType(type);
        userAuth.setIdentification(identification);
        userAuth.setCredential(credential);

        User user = signService.in(userAuth);

        session.setAttribute(Constant.SESSION_KEY_LOGIN_USER_IDX, user.getIdx());

        Map<String, Object> map = new LinkedTreeMap<>();
        map.put("succ", true);
        map.put("msg", "SUCC");

        return map;
    }

    //이메일 패스워드 가입
    @PostMapping("/up")
    public Object upPOST(
            int type,
            String identification,
            String credential
    ){
        UserAuth userAuth = new UserAuth();
        userAuth.setType(type);
        userAuth.setIdentification(identification);
        userAuth.setCredential(credential);

        log.info(""+userAuth.getType());
        log.info(userAuth.getCredential());
        User user = signService.in(userAuth);

        Map<String, Object> map = new LinkedTreeMap<>();
        map.put("succ", true);
        map.put("msg", "SUCC");

        return map;
    }

    @RequestMapping(value = "/out", method = {RequestMethod.GET, RequestMethod.POST})
    public Object out(HttpSession session){
        session.invalidate();

        Map<String, Object> map = new LinkedTreeMap<>();
        map.put("succ", true);
        map.put("msg", "SUCC");

        return map;
    }


}
