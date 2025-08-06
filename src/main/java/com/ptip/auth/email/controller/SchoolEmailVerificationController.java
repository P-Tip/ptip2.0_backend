package com.ptip.auth.email.controller;

import com.ptip.auth.email.dto.SchoolEmailRequestDto;
import com.ptip.auth.email.dto.SchoolEmailVerifyDto;
import com.ptip.auth.email.service.SchoolEmailVerificationService;
import com.ptip.auth.entity.User;
import com.ptip.auth.jwt.JwtUtil;
import com.ptip.auth.repository.UserRepository;
import com.ptip.common.dto.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/email")
@RequiredArgsConstructor
public class SchoolEmailVerificationController {

    private final SchoolEmailVerificationService emailService;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    private User getUserFromRequest(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        if (bearer == null || !bearer.startsWith("Bearer ")) {
            throw new RuntimeException("JWT 토큰이 없습니다.");
        }
        String token = bearer.substring(7);
        int userId = Integer.parseInt(jwtUtil.extractUserId(token));
        return userRepository.findById(userId).orElseThrow(() -> new RuntimeException("사용자 없음"));
    }

    /**
     * 인증번호 전송
     */
    @PostMapping("/send")
    public ResponseEntity<?> send(@RequestBody SchoolEmailRequestDto dto, HttpServletRequest request) {
        User user = getUserFromRequest(request);
        emailService.sendSchoolEmail(dto.getSchoolEmail(), user);
        return ApiResponse.success("학교 인증번호가 전송되었습니다.");
    }

    /**
     * 인증번호 검증
     */
    @PostMapping("/verify")
    public ResponseEntity<?> verify(@RequestBody SchoolEmailVerifyDto dto, HttpServletRequest request) {
        User user = getUserFromRequest(request);
        boolean result = emailService.verifySchoolEmail(dto.getSchoolEmail(), dto.getCode(), user);
        if (result) {
            return ApiResponse.success("학교 이메일 인증 완료");
        } else {
            return ApiResponse.error(HttpStatus.UNAUTHORIZED, "인증 실패");
        }
    }

    /**
     * 인증 여부 확인
     */
    @GetMapping("/check")
    public ResponseEntity<?> check(HttpServletRequest request) {
        User user = getUserFromRequest(request);
        return ApiResponse.success(emailService.isSchoolEmailVerified(user));   // ture / false
    }
}
