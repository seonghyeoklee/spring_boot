package com.study.boot1;

import com.google.gson.Gson;
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