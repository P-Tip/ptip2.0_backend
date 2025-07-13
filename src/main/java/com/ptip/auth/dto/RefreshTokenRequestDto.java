package com.ptip.auth.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RefreshTokenRequestDto {
    private String email;         // 사용자 이메일
    private String refreshToken;  // Bearer 포함 가능
}
