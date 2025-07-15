package com.ptip.auth.controller;

import com.ptip.auth.dto.LogoutRequestDto;
import com.ptip.auth.dto.RefreshTokenRequestDto;
import com.ptip.auth.entity.User;
import com.ptip.auth.jwt.JwtUtil;
import com.ptip.auth.repository.UserRepository;
import com.ptip.auth.service.KakaoAuthService;
import com.ptip.common.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
@Tag(name = "로그인", description = "카카오 로그인")
@RequiredArgsConstructor
public class AuthController {

    private final JwtUtil jwtUtil;
    private final KakaoAuthService kakaoAuthService;
    private final UserRepository userRepository;

    @Operation(summary = "카카오 로그인 콜백", description = "프론트에서 인가코드(code)로 호출")
    @GetMapping("/kakao/callback")
    public ResponseEntity<?> kakaoCallback (@RequestParam String code) throws Exception {
        // code로 카카오 토큰 → 사용자 정보 → DB 저장 → JWT 발급
        String accessToken = kakaoAuthService.kakaoLogin(code);
        return ApiResponse.success(accessToken);
    }

    @Operation(summary = "로그아웃", description = "email + refreshToken을 바디로 넘겨서 로그아웃")
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody LogoutRequestDto request) {
        String email = request.getEmail();

        // 이메일로 사용자 조회 후 refreshToken 제거
        userRepository.findAll().stream()
                .filter(u -> email.equals(u.getEmail()))
                .findFirst()
                .ifPresent(user -> {
                    user.setRefreshToken(null);
                    userRepository.save(user);
                });

        return ApiResponse.success("로그아웃 되었습니다.");
    }

    @Operation(summary = "액세스 토큰 재발급", description = "리프레시 토큰으로 새로운 액세스 토큰 발급")
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshAccessToken(@RequestBody RefreshTokenRequestDto request) {
        String email = request.getEmail();
        String rawRefreshToken = request.getRefreshToken().replace("Bearer ", "");

        // 이메일로 사용자 조회
        Optional<User> optionalUser = userRepository.findAll().stream()
                .filter(u -> email.equals(u.getEmail()))
                .findFirst();

        // refreshToken 일치 시 AccessToken 재발급
        if (optionalUser.isPresent() && rawRefreshToken.equals(optionalUser.get().getRefreshToken())) {
            String newAccessToken = jwtUtil.createAccessToken(String.valueOf(optionalUser.get().getId()));
            return ApiResponse.success(newAccessToken);
        }

        return ApiResponse.error(HttpStatus.UNAUTHORIZED, "Invalid refresh token or user not found");
    }

}
