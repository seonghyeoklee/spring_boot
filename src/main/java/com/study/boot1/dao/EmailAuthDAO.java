package com.study.boot1.dao;

import com.study.boot1.model.EmailAuth;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailAuthDAO {
    int insertEmailAuth(EmailAuth emailAuth);
    EmailAuth selectEmailAuth(@Param("token") String token);
    int updateEmailAuth(@Param("idx") Long idx);
    int updateUserAuth(@Param("idx") int userAuthIdx, @Param("prevState") int prevState, @Param("nextState") int nextState);
    int deleteUnusedEmailAuth();
}
