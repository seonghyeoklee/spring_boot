package com.study.boot1.service;

import com.study.boot1.common.AccountType;
import com.study.boot1.common.Constant;
import com.study.boot1.common.ErrorCode;
import com.study.boot1.dao.UserAuthDAO;
import com.study.boot1.dao.UserDAO;
import com.study.boot1.exception.BadRequestException;
import com.study.boot1.model.*;
import com.study.boot1.rest.KakaoUserInfoAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SignServiceImpl implements SignService{
    @Autowired
    UserDAO userDAO;

    @Autowired
    UserAuthDAO userAuthDAO;

    @Autowired
    KakaoUserInfoAPI kakaoAPI;

    @Transactional
    @Override
    public User in(UserSignParam param) {

        AccountType accountType;

        try{
            accountType =  AccountType.get(param.getType()).get();
        }catch (Exception e) {
            throw new BadRequestException(ErrorCode.INVALID_PARAM_ACCOUNT_TYPE);
        }

        switch (accountType){
            case EMAIL:
                throw new BadRequestException(ErrorCode.INVALID_PARAM_ACCOUNT_TYPE);
            case GOOGLE:
                throw new BadRequestException(ErrorCode.INVALID_PARAM_ACCOUNT_TYPE);
            case KAKAO:

                UserAuth auth = userAuthDAO.selectUserAuth(param);

                if(auth != null)
                    return auth.getUser();

                return up(param);

            case FACEBOOK:
                throw new BadRequestException(ErrorCode.INVALID_PARAM_ACCOUNT_TYPE);
            default:
                break;
        }

        return null;
    }

    @Transactional
    @Override
    public User up(UserSignParam param) {
        AccountType accountType;

        try{
            accountType =  AccountType.get(param.getType()).get();
        }catch (Exception e) {
            throw new BadRequestException(ErrorCode.INVALID_PARAM_ACCOUNT_TYPE);
        }

        switch ( accountType ) {
            case EMAIL:
                throw new BadRequestException(ErrorCode.INVALID_PARAM_ACCOUNT_TYPE);
            case GOOGLE:
                throw new BadRequestException(ErrorCode.INVALID_PARAM_ACCOUNT_TYPE);
            case KAKAO:

                String accessToken = param.getIdentification();
                KakaoUserInfo kakaoUserInfo = getKakaoUserInfo(accessToken);
                if( kakaoUserInfo == null )
                    throw new BadRequestException(0, "kakao fail");

                String kakaoId = ""+kakaoUserInfo.getId();

                UserSignParam queryParam = new UserSignParam();
                queryParam.setType(AccountType.KAKAO.intValue());
                queryParam.setIdentification(kakaoId);

                if( userAuthDAO.selectUserAuth(queryParam) != null )
                    throw new BadRequestException(0, "already exists");

                String kakaoNickname = kakaoUserInfo.getProperties().getNickname();

                User user = new User();
                user.setName(kakaoNickname);
                userDAO.insertUser(user);

                UserAuth auth = new UserAuth();
                auth.setType(AccountType.KAKAO.intValue());
                auth.setUserIdx(user.getIdx());

                userAuthDAO.insertUserAuth(auth);

                return user;

            case FACEBOOK:
                throw new BadRequestException(ErrorCode.INVALID_PARAM_ACCOUNT_TYPE);
        }

        throw new BadRequestException(ErrorCode.UNKNOWN);
    }

    private GoogleUserInfo getGoogleUserInfo(String accessToken) {
        return null;
    }

    private KakaoUserInfo getKakaoUserInfo(String accessToken) {
        try{

            KakaoUserInfo kakaoResultJson = kakaoAPI.userMeForToken("Bearer " + accessToken).execute().body();

            KakaoUserInfo kakaoResultJson2 = kakaoAPI.userMeForUserId("user_id", ""+kakaoResultJson.getId()).execute().body();

            if(kakaoResultJson2.getId() != kakaoResultJson.getId()) {
                System.out.println("kakao id not equal");
                return null;
            }

            return kakaoResultJson2;

        }catch (Exception e) {
            e.printStackTrace();
        }

        throw new RuntimeException();
    }

    private FacebookUserInfo getFacebookUserInfo(String accessToken) {
        return null;
    }
}
