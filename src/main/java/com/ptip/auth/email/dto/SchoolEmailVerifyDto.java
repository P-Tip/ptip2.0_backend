package com.ptip.auth.email.dto;

import lombok.Getter;

@Getter
public class SchoolEmailVerifyDto {
    private String schoolEmail;
    private String code;
}
