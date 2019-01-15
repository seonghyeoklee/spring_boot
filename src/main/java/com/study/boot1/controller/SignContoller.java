package com.study.boot1.controller;

import com.google.gson.internal.LinkedTreeMap;
import com.study.boot1.common.Constant;
import com.study.boot1.model.User;
import com.study.boot1.model.UserAuth;
import com.study.boot1.model.UserSignParam;
import com.study.boot1.service.SignService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
            @RequestParam int type,
            @RequestParam String identification,
            @RequestParam(required = false) String credential
    ) throws Exception{

        UserSignParam param = new UserSignParam();
        param.setType(type);
        param.setIdentification(identification);
        param.setCredential(credential);

        User user = signService.in(param);

        session.setAttribute(Constant.SESSION_KEY_LOGIN_USER_IDX, user.getIdx());

        Map<String, Object> map = new LinkedTreeMap<>();
        map.put("succ", true);
        map.put("msg", "SUCC");
        map.put("user", user);

        return map;
    }

    //이메일 패스워드 가입
    @PostMapping("/up")
    public Object upPOST(
            @RequestParam int type,
            @RequestParam String identification,
            @RequestParam(required = false) String credential
    ) throws Exception{
       UserSignParam param = new UserSignParam();
       param.setType(type);
       param.setIdentification(identification);
       param.setCredential(credential);

        User user = signService.up(param);

        Map<String, Object> map = new LinkedTreeMap<>();
        map.put("succ", true);
        map.put("msg", "SUCC");
        map.put("user", user);

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
