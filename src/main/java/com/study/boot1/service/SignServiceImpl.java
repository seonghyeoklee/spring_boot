package com.study.boot1.service;

import com.google.gson.GsonBuilder;
import com.study.boot1.common.AccountType;
import com.study.boot1.common.Constant;
import com.study.boot1.common.ErrorCode;
import com.study.boot1.dao.EmailAuthDAO;
import com.study.boot1.dao.UserAuthDAO;
import com.study.boot1.dao.UserDAO;
import com.study.boot1.exception.BadRequestException;
import com.study.boot1.model.*;
import com.study.boot1.rest.FacebookUserInfoAPI;
import com.study.boot1.rest.GoogleOAuthAPI;
import com.study.boot1.rest.GoogleUserInfoAPI;
import com.study.boot1.rest.KakaoUserInfoAPI;
import com.study.boot1.util.MailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import retrofit2.Response;

import java.io.IOException;
import java.util.Random;
import java.util.UUID;


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

    @Autowired
    FacebookUserInfoAPI facebookUserInfoAPI;

    @Autowired
    EmailAuthDAO emailAuthDAO;

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
            case EMAIL: {

                if (StringUtils.isEmpty(param.getCredential())) {
                    throw new BadRequestException(ErrorCode.INVALID_PARAM_ACCOUNT_TYPE);
                }

                // TODO : Check Email Regex
                // TODO : Check Password Regex

                UserSignParam queryParam = new UserSignParam();
                queryParam.setType(AccountType.EMAIL.intValue());
                queryParam.setIdentification(param.getIdentification());

                UserAuth userAuth = userAuthDAO.selectUserAuth(queryParam);

                if (userAuth != null && userAuth.getState() == 1) {
                    throw new BadRequestException(0, "already exists");
                }

                User user = new User();
                user.setName("nickname"); // TODO FIX
                userDAO.insertUser(user);

                UserAuth auth = new UserAuth();
                auth.setType(AccountType.EMAIL.intValue());
                auth.setUserIdx(user.getIdx());
                auth.setIdentification(param.getIdentification());
                auth.setCredential(param.getCredential());

                userAuthDAO.insertUserAuth(auth);

                int tryCount = 0;

                EmailAuth emailAuth = new EmailAuth();
                emailAuth.setUserAuthIdx(auth.getIdx());

                do {
                    emailAuth.setToken(createEmailAuthToken());

                    int insertCount = emailAuthDAO.insertEmailAuth(emailAuth);
                    if(insertCount == 1)
                        break;
                } while (tryCount++ < 5);

                if (tryCount >= 5) {
                    throw new BadRequestException(0, "email is not vaild");
                }

                MailUtil.send("관리자", auth.getIdentification(), "회원가입인증",
                        "http://localhost:8081/v1/email/auth?token="+emailAuth.getToken());

                return user;
            }

            case GOOGLE:

                String credential = param.getCredential();

                GoogleUserInfo googleUserInfo = getGoogleUserInfo(credential);

                System.out.println(new GsonBuilder().setPrettyPrinting().create().toJson(googleUserInfo));

                User googleUser = new User();
                googleUser.setName(googleUserInfo.getName());

                userDAO.insertUser(googleUser);

                UserAuth googleAuth = new UserAuth();
                googleAuth.setUserIdx(googleUser.getIdx());
                googleAuth.setIdentification(googleUserInfo.getSub());
                googleAuth.setCredential(googleUserInfo.getAccessToken());
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
                String facebookAccessToken = "EAAErCchzFG4BAPRNagm9sSsLwBtPZACvYEq3AnSXy3zkN91WG5V3iQ3eHpnbeeBYwnmBpNdntsk5m4xVraXj2W3OR5R35oI2SVaFYwFllUzL0h2NzlReIByZB9UKlsa30UHFyirWhcBgl4BNJgQADiNOEKcA2sI4kxJ1v7n6Os8HxxVwLi0uTcrTUDnSUZD";
                String fields = "id,name";

                FacebookUserInfo facebookUserInfo = getFacebookUserInfo(facebookAccessToken, fields);

                User facebookUser = new User();
                facebookUser.setName(facebookUserInfo.getName());

                userDAO.insertUser(facebookUser);

                UserAuth facebookAuth = new UserAuth();
                facebookAuth.setUserIdx(facebookUser.getIdx());
                facebookAuth.setIdentification(facebookUserInfo.getId());

                userAuthDAO.insertUserAuth(facebookAuth);

                return facebookUser;
        }

        throw new BadRequestException(ErrorCode.UNKNOWN);
    }

    private GoogleUserInfo getGoogleUserInfo(String credential) throws IOException {

        Response<GoogleOAuth> response = googleOAuthAPI.getToken(GoogleOAuthAPI.TOKEN_STATIC_FILED_MAP, credential).execute();

        if(response.body().getAccessToken().isEmpty())
            throw new NullPointerException();

        GoogleUserInfo googleUserInfo = googleUserInfoAPI.userInfo( "Bearer "+response.body().getAccessToken()).execute().body();

        if(googleUserInfo == null)
            throw new NullPointerException();

        googleUserInfo.setAccessToken(response.body().getAccessToken());

        return googleUserInfo;
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

    private FacebookUserInfo getFacebookUserInfo(String facebookAccessToken, String fields) throws IOException {

        return facebookUserInfoAPI.userInfoByToken(facebookAccessToken, fields).execute().body();
    }

    private String createEmailAuthToken() {
        String token = "";

        while(token.length() < Constant.EMAIL_AUTH_TOKEN_LENGTH) {
            token += UUID.randomUUID().toString().replaceAll("-","");
        }
        return token.substring(0, Constant.EMAIL_AUTH_TOKEN_LENGTH);
    }
}
