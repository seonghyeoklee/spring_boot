package com.study.boot1.rest;

import com.study.boot1.model.GoogleUserInfo;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface GoogleUserInfoAPI {
    String BASE_URL = "https://openidconnect.googleapis.com";

    /***
     *
     * @param authorization use "Bearer ~~~~~~"
     * @return
     */

    @GET("/v1/userinfo")
    Call<GoogleUserInfo> userInfo(@Header("Authorization") String authorization);
}


//{
//"sub": "116043867392470741281",
//"name": "김정원",
//"given_name": "정원",
//"family_name": "김",
//"profile": "https://plus.google.com/116043867392470741281",
//"picture": "https://lh5.googleusercontent.com/-oDqwW1HTrE0/AAAAAAAAAAI/AAAAAAAAAZU/KCKSo_HuvO0/photo.jpg",
//"gender": "male",
//"locale": "ko"
//}

//{
//"sub": "116043867392470741281",
//"name": "김정원",
//"given_name": "정원",
//"family_name": "김",
//"profile": "https://plus.google.com/116043867392470741281",
//"picture": "https://lh5.googleusercontent.com/-oDqwW1HTrE0/AAAAAAAAAAI/AAAAAAAAAZU/KCKSo_HuvO0/photo.jpg",
//"email": "fgfg4514@gmail.com",
//"email_verified": true,
//"gender": "male",
//"locale": "ko"
//}