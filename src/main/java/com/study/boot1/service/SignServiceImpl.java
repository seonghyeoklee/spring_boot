package com.study.boot1.service;

import com.study.boot1.common.Constant;
import com.study.boot1.common.ErrorCode;
import com.study.boot1.dao.UserAuthDAO;
import com.study.boot1.dao.UserDAO;
import com.study.boot1.exception.BadRequestException;
import com.study.boot1.model.KakaoResultJson;
import com.study.boot1.model.User;
import com.study.boot1.model.UserAuth;
import com.study.boot1.rest.KakaoAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.rmi.server.ServerCloneException;

@Service
public class SignServiceImpl implements SignService{
    @Autowired
    UserDAO userDAO;

    @Autowired
    UserAuthDAO userAuthDAO;

    @Autowired
    KakaoAPI kakaoAPI;

    @Transactional
    @Override
    public User in(UserAuth userAuth) {

        switch (userAuth.getType()){
            case Constant.ACCOUNT_TYPE_EMAIL:
                throw new BadRequestException(ErrorCode.INVALID_PARAM_ACOUNT_TYPE);
            case Constant.ACCOUNT_TYPE_GOOGLE:
                throw new BadRequestException(ErrorCode.INVALID_PARAM_ACOUNT_TYPE);
            case Constant.ACCOUNT_TYPE_KAKAO:

                UserAuth auth = userAuthDAO.selectUserAuth(userAuth);

                if(auth != null)
                    return auth.getUser();

                User user = up(userAuth);

            case Constant.ACCOUNT_TYPE_FACEBOOK:    //과제
                throw new BadRequestException(ErrorCode.INVALID_PARAM_ACOUNT_TYPE);
            default:
                break;
        }

        return null;
    }

    @Transactional
    @Override
    public User up(UserAuth userAuth) {
        switch (userAuth.getType()){
            case Constant.ACCOUNT_TYPE_EMAIL:
                throw new BadRequestException(ErrorCode.INVALID_PARAM_ACOUNT_TYPE);
            case Constant.ACCOUNT_TYPE_GOOGLE:
                throw new BadRequestException(ErrorCode.INVALID_PARAM_ACOUNT_TYPE);
            case Constant.ACCOUNT_TYPE_KAKAO:

                String accessToken = userAuth.getCredential();
                KakaoResultJson kakaoResultJson = getKakaoUser(accessToken);

                if(kakaoResultJson == null){
                    throw new BadRequestException(0, "kakao fail"); //과제
                }

                String kakaId = "" + kakaoResultJson.getId();
                String KakaoNickName = kakaoResultJson.getProperties().getNickname();

                UserAuth auth = new UserAuth();
                auth.setType(Constant.ACCOUNT_TYPE_KAKAO);
                auth.setIdentification(kakaId);

                if(userAuthDAO.selectUserAuth(auth) != null)
                    throw new BadRequestException(0, "already exists");

                User user = new User();
                user.setName(KakaoNickName);
                userDAO.insertUser(user);

                auth.setUserIdx(user.getIdx());
                userAuthDAO.insertUserAuth(auth);

                return user;

            case Constant.ACCOUNT_TYPE_FACEBOOK:    //과제
                throw new BadRequestException(ErrorCode.INVALID_PARAM_ACOUNT_TYPE);
            default:
                break;
        }

        return null;
    }

    private KakaoResultJson getKakaoUser(String accessToken) throws RuntimeException {
        try{

            KakaoResultJson kakaoResultJson = kakaoAPI.userMeForToken("Bearer " + accessToken).execute().body();
            //sendApi(access_token);

            //email 값이 안넘어옴
            /*if(kakaoResultJson.getKaccount_email() == null || !kakaoResultJson.isKaccount_email_verified())
                return null;*/

            //KakaoResultJson kakaoResultJson2 = checkIdByAdminKey(kakaoResultJson.id);
            //밑의 코드가 위의 코드를 대체
            KakaoResultJson kakaoResultJson2 = kakaoAPI.userMeForUserId("user_id", ""+kakaoResultJson.getId()).execute().body();

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
}
