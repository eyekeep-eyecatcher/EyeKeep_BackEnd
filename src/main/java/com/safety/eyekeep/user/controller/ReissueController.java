package com.safety.eyekeep.user.controller;

import com.safety.eyekeep.user.handler.CreateCookie;
import com.safety.eyekeep.user.jwt.JWTUtil;
import com.safety.eyekeep.user.service.RedisService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class ReissueController {

    private final JWTUtil jwtUtil;
    private final RedisService redisService;
    private final CreateCookie createCookie;

    @GetMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {

        // 쿠키에 존재하는 refreshToken을 가져오자
        String refresh = null;
        Cookie[] cookies = request.getCookies();
        if(cookies == null) {
            Map<String, Object> message = new HashMap<>();
            message.put("message", "Cookie is null");
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }
        for(Cookie cookie : cookies) {
            if("refresh".equals(cookie.getName())) {
                refresh = cookie.getValue();
            }
        }

        // 검증 시작
        // refreshToken이 없는 경우
        if(refresh == null) {
            Map<String, Object> message = new HashMap<>();
            message.put("message", "Refresh token is null");
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }

        // 유효기간 확인
        // 토큰이 refresh인지 확인
        String category;
        try {
            category = jwtUtil.getCategory(refresh);
        } catch (ExpiredJwtException e) {
            Map<String, Object> message = new HashMap<>();
            message.put("message", "Refresh token is expired");
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }

        if(!category.equals("refresh")) {
            Map<String, Object> message = new HashMap<>();
            message.put("message", "Invalid refresh token");
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }

        // 새로운 Token을 만들기 위해 준비
        String username = jwtUtil.getUsername(refresh);
        String role = jwtUtil.getRole(refresh);

        // Redis내에 존재하는 refreshToken인지 확인
        if(redisService.checkExistsValue(redisService.getValues(username))) {
            Map<String, Object> message = new HashMap<>();
            message.put("message", "No exists in redis refresh token");
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }

        // 새로운 JWT Token 생성
        String newAccessToken = jwtUtil.createJwt("access", username, role, 300000L);
        String newRefreshToken = jwtUtil.createJwt("refresh", username, role, 86400000L);

        // update refreshToken to Redis
        redisService.setValues(username, newRefreshToken, Duration.ofMillis(86400000L));

        // 응답
        response.setHeader("access", "Bearer " + newAccessToken);
        response.addCookie(createCookie.createCookie("refresh", newRefreshToken));

        Map<String, Object> message = new HashMap<>();
        message.put("message", "Reissue refresh token success");
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}