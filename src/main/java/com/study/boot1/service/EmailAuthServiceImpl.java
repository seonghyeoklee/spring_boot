package com.study.boot1.service;

import com.study.boot1.common.Constant;
import com.study.boot1.dao.EmailAuthDAO;
import com.study.boot1.exception.BadRequestException;
import com.study.boot1.model.EmailAuth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class EmailAuthServiceImpl implements EmailAuthService {

    @Autowired
    EmailAuthDAO emailAuthDAO;

    @Override
    public Object auth(String token) {
        token = token.trim();

        if(token.length() != Constant.EMAIL_AUTH_TOKEN_LENGTH) {
            throw new BadRequestException(0, "not found");
        }

        EmailAuth emailAuth = emailAuthDAO.selectEmailAuth(token);

        if(emailAuth == null) {
            throw new BadRequestException(0, "not found");
        }

        if(emailAuth.getAuthAt() != null) {
            throw new BadRequestException(0, "already auth");
        }

        if(System.currentTimeMillis() - emailAuth.getCreatedAt().getTime() > Constant.EXPIRE_EMAIL_AUTH_MILLIS) {
            throw new BadRequestException(0, "expired");
        }

        int updateCount = emailAuthDAO.updateEmailAuth(emailAuth.getIdx());

        if(updateCount <= 0) {
            throw new BadRequestException(0, "fail to update auth");
        }

        updateCount = emailAuthDAO.updateUserAuth(emailAuth.getUserAuthIdx(), Constant.EMAIL_TYPE_USER_AUTH_DEFAULT_STATE, Constant.USER_AUTH_DEFAULT_STATE);

        if(updateCount <= 0) {
            throw new BadRequestException(0, "fail to update auth");
        }

        return new ResponseEntity<Object>(HttpStatus.OK);
    }

    @Scheduled(cron = "0 0 1 * * *")
    public void removeUnusedEmailAuth(){
        emailAuthDAO.deleteUnusedEmailAuth();
    }
}
