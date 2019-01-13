package com.study.boot1;

import com.google.gson.Gson;
import com.study.boot1.rest.FacebookUserInfoAPI;
import com.study.boot1.rest.GoogleOAuthAPI;
import com.study.boot1.rest.KakaoUserInfoAPI;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Configuration
public class RetrofitConfig {

    @Bean
    public KakaoUserInfoAPI getKakaoAPI() {
        return getRetrofit(KakaoUserInfoAPI.BASE_URL).create(KakaoUserInfoAPI.class);
    }

    @Bean
    public GoogleOAuthAPI getGoogleOAuthAPI(){
        return getRetrofit(GoogleOAuthAPI.BASE_URL).create(GoogleOAuthAPI.class);
    }

    @Bean
    public FacebookUserInfoAPI getFacebookUserInfoAPI(){
        return getRetrofit(FacebookUserInfoAPI.BASE_URL).create(FacebookUserInfoAPI.class);
    }

    private Retrofit getRetrofit(String BASE_URL) {
        OkHttpClient client = new OkHttpClient.Builder()
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .client(client)
                .build();
        return retrofit;
    }
}