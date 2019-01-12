package com.study.boot1.model;

import lombok.Data;

@Data
public class KakaoUserInfo {
    private long id;
    private String kaccount_email = null;
    private boolean kaccount_email_verified;
    private KakaoProperties properties = null;

    @Data
    public class KakaoProperties {
        private String nickname = null;
    }
}

