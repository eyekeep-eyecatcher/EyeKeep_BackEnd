package com.safety.eyekeep.user.controller;

import com.safety.eyekeep.user.jwt.JWTUtil;
import com.safety.eyekeep.user.service.RedisService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class SignoutController {

    private final JWTUtil jwtUtil;
    private final RedisService redisService;

    @PostMapping("/signout")
    public ResponseEntity<?> signout(HttpServletRequest request) {
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
            message.put("message", "Refresh token expired");
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }

        if(!category.equals("refresh")) {
            Map<String, Object> message = new HashMap<>();
            message.put("message", "Invalid refresh token");
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }

        String key = jwtUtil.getUsername(refresh);

        if(redisService.checkExistsValue(redisService.getValues(key))) {
            Map<String, Object> message = new HashMap<>();
            message.put("message", "No exists in redis refresh token");
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }

        redisService.deleteValues(key);

        Map<String, Object> message = new HashMap<>();
        message.put("message", "Signed out successfully");
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}