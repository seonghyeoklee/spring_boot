package com.study.boot1.service;

import com.google.gson.GsonBuilder;
import com.study.boot1.common.AccountType;
import com.study.boot1.common.ErrorCode;
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
import retrofit2.Response;

import java.util.Random;


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
                Random rand = new Random();
                String numStr = "";

                for(int i=0;i<6;i++) {
                    String ran = Integer.toString(rand.nextInt(10));

                    numStr += ran + " ";
                }

                String fromName = "스프링";
                String to = "dltjdgur327@naver.com";
                String title = "[스프링] 이메일 주소 확인 요청";
                /*String content = "해당 이메일 주소가 고객님 소유임을 확인하려면, 아래의 코드를 확인 페이지에 입력하십시오.\n" +
                        numStr +
                        "해당 코드는 이메일 발송 3시간 후 만료됩니다.";*/

                //String content = new StringBuffer().append("<h1> 메일인증 </h1>").append("<a href='http://localhost:80/v1/sign/test'>email 인증확인</a>").toString();

                String content = "<h2> 메일인증 </h2><a href='http://localhost:80/v1/sign/test'>email 인증확인2</a>";

                MailUtil mailUtil = new MailUtil();
                mailUtil.send(fromName, to, title, content);

                //메일인증 완료시 user정보 저장

                return null;

            case GOOGLE:

                String credential = param.getCredential();

                Response<GoogleOAuth> response = googleOAuthAPI.getToken(GoogleOAuthAPI.TOKEN_STATIC_FILED_MAP, credential).execute();

                if(response.body().getAccessToken().isEmpty())
                    throw new NullPointerException();

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

                Response<FacebookUserInfo> facebookUserInfoResponse = facebookUserInfoAPI.userInfoByToken("EAAErCchzFG4BAPRNagm9sSsLwBtPZACvYEq3AnSXy3zkN91WG5V3iQ3eHpnbeeBYwnmBpNdntsk5m4xVraXj2W3OR5R35oI2SVaFYwFllUzL0h2NzlReIByZB9UKlsa30UHFyirWhcBgl4BNJgQADiNOEKcA2sI4kxJ1v7n6Os8HxxVwLi0uTcrTUDnSUZD", "id,name,email").execute();

                User facebookUser = new User();
                facebookUser.setName(facebookUserInfoResponse.body().getName());

                userDAO.insertUser(facebookUser);

                UserAuth facebookAuth = new UserAuth();
                facebookAuth.setUserIdx(facebookUser.getIdx());
                facebookAuth.setIdentification(facebookUserInfoResponse.body().getId());

                userAuthDAO.insertUserAuth(facebookAuth);

                return facebookUser;
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
