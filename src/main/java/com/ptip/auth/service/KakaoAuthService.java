package com.ptip.auth.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ptip.auth.dto.KakaoUserInfoDto;
import com.ptip.auth.entity.User;
import com.ptip.auth.jwt.JwtUtil;
import com.ptip.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.*;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class KakaoAuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.client-secret}")
    private String clientSecret;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    /**
     * 인가코드(code)를 받아 카카오 access token을 발급받고,
     * 사용자 정보를 가져와 DB에 저장하거나 업데이트 후
     * JWT AccessToken을 발급한다.
     */
    public String kakaoLogin(String code) throws Exception {
        String accessToken = getKakaoAccessToken(code);     // 카카오 토큰 요청
        KakaoUserInfoDto kakaoUser = getUserInfo(accessToken); // 사용자 정보 요청

        // 기존 유저 확인
        Optional<User> optionalUser = userRepository.findByKakaoId(kakaoUser.getKakaoId());

        // 새 RefreshToken 발급
        String refreshToken = jwtUtil.createRefreshToken();

        // 기존 유저는 refreshToken만 갱신, 없으면 새로 저장
        User user = optionalUser.map(u -> {
            u.setRefreshToken(refreshToken);
            return u;
        }).orElseGet(() -> User.builder()
                .kakaoId(kakaoUser.getKakaoId())
                .nickname(kakaoUser.getNickname())
                .email(kakaoUser.getEmail())
                .img(kakaoUser.getImg())
                .refreshToken(refreshToken)
                .build());

        userRepository.save(user); // 저장 또는 갱신
        return jwtUtil.createAccessToken(String.valueOf(user.getId()));
    }

    /**
     * 카카오에 POST 요청을 보내 access token을 발급받는다.
     */
    private String getKakaoAccessToken(String code) throws Exception {
        HttpClient client = HttpClient.newHttpClient();

        // Client Secret 포함
        String body = "grant_type=authorization_code"
                + "&client_id=" + clientId
                + "&client_secret=" + clientSecret
                + "&redirect_uri=" + redirectUri
                + "&code=" + code;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://kauth.kakao.com/oauth/token"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // 응답 확인 (디버깅 목적)
        System.out.println("카카오 토큰 응답: " + response.body());

        JsonNode json = objectMapper.readTree(response.body());

        JsonNode accessTokenNode = json.get("access_token");
        if (accessTokenNode == null) {
            throw new RuntimeException("카카오 토큰 발급 실패: " + response.body());
        }

        return accessTokenNode.asText();
    }

    /**
     * 발급받은 카카오 access token을 통해 사용자 정보를 가져온다.
     */
    private KakaoUserInfoDto getUserInfo(String accessToken) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://kapi.kakao.com/v2/user/me"))
                .header("Authorization", "Bearer " + accessToken)
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        JsonNode json = objectMapper.readTree(response.body());

        // 사용자 정보 파싱
        String id = json.get("id").asText();
        String nickname = json.get("properties").get("nickname").asText();
        String img = json.get("properties").get("profile_image").asText();
        String email = json.get("kakao_account").get("email").asText();

        return new KakaoUserInfoDto(id, nickname, email, img);
    }
}
