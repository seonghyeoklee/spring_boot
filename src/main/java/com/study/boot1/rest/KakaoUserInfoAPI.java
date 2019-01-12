package com.study.boot1.rest;

import com.study.boot1.common.Constant;
import com.study.boot1.model.KakaoUserInfo;
import retrofit2.Call;
import retrofit2.http.*;

public interface KakaoUserInfoAPI {
    String BASE_URL = "https://kapi.kakao.com";

    @GET("v1/user/me")
    Call<KakaoUserInfo> userMeForToken(@Header("Authorization") String authorization);

    @POST("/v1/user/me")
    @Headers("Authorization: KakaoAK " + Constant.KAKAO_ADMIN_KEY)
    @FormUrlEncoded
    Call<KakaoUserInfo> userMeForUserId(
            @Field("target_id_type") String targetIdType,
            @Field("target_id") String targetId);
}
