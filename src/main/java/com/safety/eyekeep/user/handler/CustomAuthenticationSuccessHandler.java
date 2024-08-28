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
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JWTUtil jwtUtil;
    private final RedisService redisService;
    private final CreateCookie createCookie;
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        UserDetails userDetail = (CustomUserDetails) authentication.getPrincipal();

        // 토큰 생성시에 사용자명과 권한이 필요하니 준비하자
        String username = userDetail.getUsername();

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
        Map<String, Object> msg = new HashMap<>();
        msg.put("message", "Login success");
        response.setHeader("access", "Bearer " + accessToken);
        response.addCookie(createCookie.createCookie("refresh", refreshToken));
        response.setStatus(HttpStatus.OK.value());
        response.setContentType("application/json"); // 응답 타입 설정

        // ObjectMapper를 사용하여 Map을 JSON으로 변환
        String jsonResponse = objectMapper.writeValueAsString(msg);

        // JSON 응답 본문에 작성
        response.getWriter().write(jsonResponse);
    }
}