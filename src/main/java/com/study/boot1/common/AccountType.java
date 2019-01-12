package com.study.boot1.common;

import java.util.Arrays;
import java.util.Optional;

public enum AccountType {
    EMAIL(1),
    GOOGLE(2),
    KAKAO(3),
    FACEBOOK(4);

    public static Optional<AccountType> get(final int intValue){
        return Arrays.stream(values()).filter(at -> at.intValue() == intValue).findFirst();
    }

    private int value;

    AccountType(int value) {
        this.value = value;
    }

    public int intValue() {
        return value;
    }
}
