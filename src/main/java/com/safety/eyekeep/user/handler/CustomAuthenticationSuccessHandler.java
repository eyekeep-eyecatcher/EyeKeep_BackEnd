package com.safety.eyekeep.user.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safety.eyekeep.user.domain.CustomUserDetails;
import com.safety.eyekeep.user.jwt.JWTUtil;
import com.safety.eyekeep.user.service.RedisService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.util.Collection;
import java.util.Iterator;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {//implements AuthenticationSuccessHandler {

    // private final ObjectMapper objectMapper;

    private final JWTUtil jwtUtil;
    private final RedisService redisService;
    private final CreateCookie createCookie;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) {
        // objectMapper.writeValue(response.getWriter(), user);

        UserDetails customUserDetail = (CustomUserDetails) authentication.getPrincipal();

        // 토큰 생성시에 사용자명과 권한이 필요하니 준비하자
        String username = customUserDetail.getUsername();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        // accessToken과 refreshToken 생성
        String accessToken = jwtUtil.createJwt("access", username, role, 300000L);
        String refreshToken = jwtUtil.createJwt("refresh", username, role, 86400000L);

        // redis에 insert (key = username / value = refreshToken)
        redisService.setValues(username, refreshToken, Duration.ofMillis(86400000L));

        // 응답
        response.setHeader("access", "Bearer " + accessToken);
        response.addCookie(createCookie.createCookie("refresh", refreshToken));
        response.setStatus(HttpStatus.OK.value());
        /*response.sendRedirect("http://localhost:8080/");*/        // 로그인 성공시 프론트에 알려줄 redirect 경로
    }
}