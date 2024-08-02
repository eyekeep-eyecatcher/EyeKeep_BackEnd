package com.safety.eyekeep.user.login;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safety.eyekeep.user.dto.LoginDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StringUtils;

import java.io.IOException;

public class CustomAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public CustomAuthenticationFilter() {
        // url과 일치할 경우 해당 필터가 동작합니다.
        super(new AntPathRequestMatcher("/signin"));
    }
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {

        // 해당 요청이 POST 인지 확인
        if(!isPost(request)) {
            throw new IllegalStateException("Authentication is not supported");
        }

        // POST 이면 body 를 LoginDto( 로그인 정보 DTO ) 에 매핑
        LoginDTO loginDto = objectMapper.readValue(request.getReader(), LoginDTO.class);

        // ID, PASSWORD 가 있는지 확인
        if(!StringUtils.hasLength(loginDto.getUsername())
                || !StringUtils.hasLength(loginDto.getPassword())) {
            throw new IllegalArgumentException("username or password is empty");
        }

        // 처음에는 인증 되지 않은 토큰 생성
        CustomAuthenticationToken token = new CustomAuthenticationToken(
                loginDto.getUsername(),
                loginDto.getPassword()
        );

        // Manager 에게 인증 처리
        return getAuthenticationManager().authenticate(token);
    }

    private boolean isPost(HttpServletRequest request) {

        return "POST".equals(request.getMethod());
    }

}