package com.safety.eyekeep.user.controller;

import com.safety.eyekeep.user.domain.CustomUserDetails;
import com.safety.eyekeep.user.domain.UserEntity;
import com.safety.eyekeep.user.dto.RoleDTO;
import com.safety.eyekeep.user.handler.CreateCookie;
import com.safety.eyekeep.user.jwt.JWTUtil;
import com.safety.eyekeep.user.repository.UserRepository;
import com.safety.eyekeep.user.service.RedisService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class SelectRoleController {

    private final JWTUtil jwtUtil;
    private final RedisService redisService;
    private final CreateCookie createCookie;

    private final UserRepository userRepository;

    @PostMapping("/role")
    public ResponseEntity<?> selectRole(@RequestBody RoleDTO roleDTO, HttpServletResponse response, Authentication authentication) {
        String role = roleDTO.getRole();
        if(!role.equals("Parent") && !role.equals("Child") && !role.equals("Admin")) {
            Map<String, Object> message = new HashMap<>();
            message.put("message", "Invalid role");
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        String username = customUserDetails.getUsername();
        UserEntity userEntity = userRepository.findByUsername(username);

        if(userEntity == null) {
            Map<String, Object> message = new HashMap<>();
            message.put("message", "Invalid user");
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }
        userEntity.setRole(role);

        userRepository.save(userEntity);

        // role을 입력한 새로운 Token을 만들기 위해 준비

        // 새로운 JWT Token 생성
        String newAccessToken = jwtUtil.createJwt("access", username, role, 300000L);
        String newRefreshToken = jwtUtil.createJwt("refresh", username, role, 86400000L);

        // update refreshToken to Redis
        redisService.setValues(username, newRefreshToken, Duration.ofMillis(86400000L));

        // 응답
        response.setHeader("access", "Bearer " + newAccessToken);
        response.addCookie(createCookie.createCookie("refresh", newRefreshToken));

        Map<String, Object> message = new HashMap<>();
        message.put("message", role);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
