package com.study.boot1.model;

import lombok.Data;

@Data
public class KakaoResultJson{
    private long id;
    private String kaccount_email;
    private boolean kaccount_email_verified;
    private KakaoProperties properties;

    @Data
    public class KakaoProperties{
        private String nickname;
    }
}