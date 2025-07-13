package com.ptip.auth.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KakaoUserInfoDto {
    private String kakaoId;
    private String nickname;
    private String email;
    private String img;
}

