package com.study.boot1.service;

import com.study.boot1.model.User;
import com.study.boot1.model.UserSignParam;

public interface SignService {
    public final static String SIGN_IN_USER_IDX_KEY = "SignInUserIdx";
    public final static String AUTOSIGN_KEY = "Autosign";

    User in(UserSignParam param);
    User up(UserSignParam param);
}
