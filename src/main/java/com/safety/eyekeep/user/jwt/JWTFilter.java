package com.safety.eyekeep.user.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safety.eyekeep.user.domain.CustomUserDetails;
import com.safety.eyekeep.user.domain.UserEntity;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    // @RequiredArgsConstructor 통해 생성자 주입
    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        // 요청 헤더에 있는 access라는 값을 가져오자 이게 accessToken이다.
        String accessToken = request.getHeader("access");

        // 요청헤더에 access가 없는 경우
        if (accessToken == null) {
            filterChain.doFilter(request, response);
            return;
        }

        // Bearer 제거 <- oAuth2를 이용했다고 명시적으로 붙여주는 타입인데 JWT를 검증하거나 정보를 추출 시 제거해줘야한다.
        String originToken = accessToken.substring(7);

        // 유효한지 확인 후 클라이언트로 상태 코드 응답
        String category;
        try {
            // accessToken인지 refreshToken인지 확인
            category = jwtUtil.getCategory(originToken);
        } catch (ExpiredJwtException e) {
            // 만약 ExpiredJwtException이 발생하면, 응답을 생성합니다.
            // HTTP 응답 상태 코드를 401 (Unauthorized)로 설정합니다.
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            Map<String, Object> msg = new HashMap<>();
            msg.put("message", "Access token expired");
            objectMapper.writeValue(response.getWriter(), msg);
            return;
        }

        // JWTFilter는 요청에 대해 accessToken만 취급하므로 access인지 확인
        if (!"access".equals(category)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            Map<String, Object> msg = new HashMap<>();
            msg.put("message", "Invalid access token");
            objectMapper.writeValue(response.getWriter(), msg);
            return;
        }

        // 사용자명과 권한을 accessToken에서 추출
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(jwtUtil.getUsername(originToken));
        userEntity.setPassword(null);
        userEntity.setNickname(null);
        userEntity.setRole(jwtUtil.getRole(originToken));

        CustomUserDetails customUserDetails = new CustomUserDetails(userEntity);

        Authentication authentication = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }
}
