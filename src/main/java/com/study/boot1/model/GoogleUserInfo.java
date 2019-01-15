package com.study.boot1.model;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoogleUserInfo {
    private String sub;
    private String name;
    private String profile;
    private String picture;
    private String email;

    @SerializedName("email_verified")
    private Boolean emailVerified;

    private String gender;
    private String locale;
}
