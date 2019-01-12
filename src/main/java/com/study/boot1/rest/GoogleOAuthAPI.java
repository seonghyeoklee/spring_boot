package com.study.boot1.rest;

import com.study.boot1.common.Constant;
import com.study.boot1.model.GoogleOAuth;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

import java.util.HashMap;
import java.util.Map;

public interface GoogleOAuthAPI {
    String BASE_URL = "https://www.googleapis.com";

    Map<String, String> TOKEN_STATIC_FILED_MAP = new HashMap<String, String>() {{
        put("client_id", Constant.GOOGLE_API_CLIENT_ID);
        put("client_secret", Constant.GOOGLE_API_CLIENT_SECRET);
        put("redirect_uri", Constant.GOOGLE_API_REDIRECT_URI);
        put("grant_type", "authorization_code");
    }};

    @POST("/oauth2/v4/token")
    @FormUrlEncoded
    Call<GoogleOAuth> getToken(@FieldMap Map<String, String> staticFieldMap, @Field("code") String code);
}
