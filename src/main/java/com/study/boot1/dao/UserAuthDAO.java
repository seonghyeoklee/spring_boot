package com.study.boot1.dao;

import com.study.boot1.model.UserAuth;
import com.study.boot1.model.UserSignParam;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAuthDAO {
    int insertUserAuth(UserAuth userAuth);

    UserAuth selectUserAuth(UserSignParam param);
}
