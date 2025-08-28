package com.ptip.like.Controller;

import com.ptip.auth.entity.User;
import com.ptip.auth.jwt.JwtUtil;
import com.ptip.auth.repository.UserRepository;
import com.ptip.auth.service.KakaoAuthService;
import com.ptip.common.dto.ApiResponse;
import com.ptip.common.exception.ResourceNotFoundException;
import com.ptip.like.Service.LikeService;
import com.ptip.like.domain.TargetType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/likes")
public class LikeController {

    private final KakaoAuthService authService;
    private final LikeService likeService;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public LikeController(KakaoAuthService authService, LikeService likeService, UserRepository userRepository, JwtUtil jwtUtil) {
        this.authService = authService;
        this.likeService = likeService;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("")
    public ResponseEntity<?> toggleLike(
            @RequestParam("targetType") TargetType targetType,
            @RequestParam("targetId") int targetId,
            @RequestHeader("Authorization") String token
    ) {
        String userId = jwtUtil.extractUserId(token.substring(7));
        User user = userRepository.findById(Integer.parseInt(userId)).orElseThrow(() -> new ResourceNotFoundException("해당 id의 사용자를 찾을 수 없습니다."));
        String result = likeService.toggleLike(user, targetType, targetId);
        return ApiResponse.success(result);
    }
}
