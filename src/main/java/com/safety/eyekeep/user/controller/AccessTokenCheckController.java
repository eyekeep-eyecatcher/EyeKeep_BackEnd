package com.safety.eyekeep.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class AccessTokenCheckController {

    @PostMapping("/check/access")
    public ResponseEntity<?> checkAccessToken() {
        Map<String, Object> message = new HashMap<>();
        message.put("message", "Access token is active");
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @PostMapping("/check/role")
    public ResponseEntity<?> checkRole(Authentication authentication) {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        if(!(Objects.equals(role, "Parent") || Objects.equals(role, "Child") || Objects.equals(role, "Admin"))) {
            Map<String, Object> message = new HashMap<>();
            message.put("message", "Role is not allowed");
            return new ResponseEntity<>(message, HttpStatus.FORBIDDEN);
        }

        Map<String, Object> message = new HashMap<>();
        message.put("message", role);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
