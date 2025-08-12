package org.coupe.springbootdeveloper.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.coupe.springbootdeveloper.repository.RefreshTokenRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthController {
    private final RefreshTokenRepository refreshTokenRepository;

    @DeleteMapping("/refresh-token")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        // 요청 헤더에서 리프레시 토큰 추출 (예: 쿠키에서 또는 Authorization 헤더)
        String refreshToken = extractRefreshToken(request);

        if (refreshToken != null) {
            refreshTokenRepository.deleteByRefreshToken(refreshToken);
        }
        return ResponseEntity.ok().build();
    }

    private String extractRefreshToken(HttpServletRequest request) {
        // 쿠키 또는 헤더에서 리프레시 토큰 추출 로직 구현
        // 예를 들어, 쿠키에서 꺼내는 경우:
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("refresh_token".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
