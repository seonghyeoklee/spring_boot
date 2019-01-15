package com.study.boot1.service;

import com.google.gson.GsonBuilder;
import com.study.boot1.common.AccountType;
import com.study.boot1.common.ErrorCode;
import com.study.boot1.dao.UserAuthDAO;
import com.study.boot1.dao.UserDAO;
import com.study.boot1.exception.BadRequestException;
import com.study.boot1.model.*;
import com.study.boot1.rest.GoogleOAuthAPI;
import com.study.boot1.rest.GoogleUserInfoAPI;
import com.study.boot1.rest.KakaoUserInfoAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import retrofit2.Response;


@Service
public class SignServiceImpl implements SignService{
    @Autowired
    UserDAO userDAO;

    @Autowired
    UserAuthDAO userAuthDAO;

    @Autowired
    KakaoUserInfoAPI kakaoAPI;

    @Autowired
    GoogleOAuthAPI googleOAuthAPI;

    @Autowired
    GoogleUserInfoAPI googleUserInfoAPI;

    @Transactional
    @Override
    public User in(UserSignParam param) throws Exception{

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
    public User up(UserSignParam param) throws Exception{
        AccountType accountType;

        try{
            accountType =  AccountType.get(param.getType()).get();
        }catch (Exception e) {
            throw new BadRequestException(ErrorCode.INVALID_PARAM_ACCOUNT_TYPE);
        }

        switch ( accountType ) {
            case EMAIL:
                //email 발송해서 인증처리

                return null;
            case GOOGLE:
                String credential = param.getCredential();

                Response<GoogleOAuth> response = googleOAuthAPI.getToken(GoogleOAuthAPI.TOKEN_STATIC_FILED_MAP, credential).execute();
                GoogleUserInfo googleUserInfo = googleUserInfoAPI.userInfo( "Bearer "+response.body().getAccessToken()).execute().body();

                if(googleUserInfo == null)
                    throw new NullPointerException();

                System.out.println(new GsonBuilder().setPrettyPrinting().create().toJson(googleUserInfo));

                User googleUser = new User();
                googleUser.setName(googleUserInfo.getName());

                userDAO.insertUser(googleUser);

                UserAuth googleAuth = new UserAuth();
                googleAuth.setUserIdx(googleUser.getIdx());
                googleAuth.setIdentification(googleUserInfo.getSub());
                googleAuth.setCredential(response.body().getAccessToken());
                googleAuth.setType(AccountType.GOOGLE.intValue());

                userAuthDAO.insertUserAuth(googleAuth);

                return googleUser;

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

                User kakaoUser = new User();
                kakaoUser.setName(kakaoNickname);

                userDAO.insertUser(kakaoUser);

                UserAuth kakaoAuth = new UserAuth();
                kakaoAuth.setType(AccountType.KAKAO.intValue());
                kakaoAuth.setUserIdx(kakaoUser.getIdx());

                userAuthDAO.insertUserAuth(kakaoAuth);

                return kakaoUser;

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
