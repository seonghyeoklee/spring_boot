package com.study.boot1.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FacebookUserInfo {
    private String id;
    private String name;
    private String emalil;
}
