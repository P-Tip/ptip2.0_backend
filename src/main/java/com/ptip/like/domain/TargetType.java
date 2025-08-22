package com.ptip.like.domain;

import java.util.Arrays;

public enum TargetType {
    장학금, 교내외;

    public static TargetType parsing(String value) {
        for (TargetType t : values()) {
            if (t.name().equals(value)) {
                return t;
            }
        }
        throw new IllegalArgumentException("유효하지 않은 TargetType: " + value);
    }
}
