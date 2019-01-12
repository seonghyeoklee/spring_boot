package com.study.boot1.rest;


import com.study.boot1.model.FacebookUserInfo;
import retrofit2.Call;
import retrofit2.http.*;

public interface FacebookUserInfoAPI {
    String BASE_URL = "https://graph.facebook.com/v3.2/";

    @POST("me")
    @FormUrlEncoded
    Call<FacebookUserInfo> userInfoByToken(@Field("access_token") String accessToken, @Field("fields") String fields);

//    {
//        "id": "842386685881185",
//        "name": "Kim  JungWon",
//        "email": "dfdf4514@hanmail.net"
//    }

    @GET("{id}")
    Call<FacebookUserInfo> userInfoByAdminKey(@Query("access_token") String adminKey);

//    {
//        "name": "김정원",
//        "id": "842386685881185"
//    }
}