package com.safety.eyekeep.user.controller;

import com.safety.eyekeep.user.jwt.JWTUtil;
import com.safety.eyekeep.user.service.RedisService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SignoutController {

    private final JWTUtil jwtUtil;
    private final RedisService redisService;

    @PostMapping("/signout")
    public ResponseEntity<?> signout(HttpServletRequest request, HttpServletResponse response) {
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

        String key = jwtUtil.getUsername(refresh);

        if(redisService.checkExistsValue(redisService.getValues(key))) {
            return new ResponseEntity<>("No exists in redis refresh token", HttpStatus.BAD_REQUEST);
        }

        redisService.deleteValues(key);

        Cookie cookie = new Cookie("refresh", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");

        response.addCookie(cookie);
        return new ResponseEntity<>("Signout complete", HttpStatus.OK);
    }
}