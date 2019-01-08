package com.study.boot1.rest;

import com.study.boot1.common.Constant;
import com.study.boot1.model.KakaoResultJson;
import retrofit2.Call;
import retrofit2.http.*;

public interface KakaoAPI {

    String BASE_URL = "https://kapi.kakao.com";

    @GET("/v1/user/me")
    Call<KakaoResultJson> userMeForToken(@Header("Authorization") String authorization);

    @FormUrlEncoded
    @POST("/v1/user/me")
    @Headers("Authorization: KakaoAK " + Constant.KAKAO_ADMIN_KEY)
    Call<KakaoResultJson> userMeForUserId(
            @Field("target_id_type") String targetIdType,
            @Field("target_id") String targetId
    );
}
