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
            return new ResponseEntity<>("Cookie is null", HttpStatus.BAD_REQUEST);
        }
        for(Cookie cookie : cookies) {
            if("refresh".equals(cookie.getName())) {
                refresh = cookie.getValue();
            }
        }

        // 검증 시작
        // refreshToken이 없는 경우
        if(refresh == null) {
            return new ResponseEntity<>("Refresh token is null", HttpStatus.BAD_REQUEST);
        }

        // 유효기간 확인
        // 토큰이 refresh인지 확인
        String category;
        try {
            category = jwtUtil.getCategory(refresh);
        } catch (ExpiredJwtException e) {
            return new ResponseEntity<>("Refresh token expired", HttpStatus.BAD_REQUEST);
        }

        if(!category.equals("refresh")) {
            return new ResponseEntity<>("Invalid refresh token", HttpStatus.BAD_REQUEST);
        }

        // 새로운 Token을 만들기 위해 준비
        String username = jwtUtil.getUsername(refresh);
        String role = jwtUtil.getRole(refresh);

        // Redis내에 존재하는 refreshToken인지 확인
        if(redisService.checkExistsValue(redisService.getValues(username))) {
            return new ResponseEntity<>("No exists in redis refresh token", HttpStatus.BAD_REQUEST);
        }

        // 새로운 JWT Token 생성
        String newAccessToken = jwtUtil.createJwt("access", username, role, 300000L);
        String newRefreshToken = jwtUtil.createJwt("refresh", username, role, 86400000L);

        // update refreshToken to Redis
        redisService.setValues(username, newRefreshToken, Duration.ofMillis(86400000L));

        // 응답
        response.setHeader("access", "Bearer " + newAccessToken);
        response.addCookie(createCookie.createCookie("refresh", newRefreshToken));

        return new ResponseEntity<>(HttpStatus.OK);
    }
}