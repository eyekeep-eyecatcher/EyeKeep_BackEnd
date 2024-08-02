package com.safety.eyekeep.user.controller;

import com.safety.eyekeep.user.domain.CustomOAuth2User;
import com.safety.eyekeep.user.domain.UserEntity;
import com.safety.eyekeep.user.dto.JoinDTO;
import com.safety.eyekeep.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SelectRoleController {

    private final UserRepository userRepository;

    @PostMapping("/role")
    public ResponseEntity<?> selectRole(@RequestBody JoinDTO joinDTO, Authentication authentication) {
        String role = joinDTO.getRole();
        if(!role.equals("Parent") && !role.equals("Child")) {
            return new ResponseEntity<>("Invalid role", HttpStatus.BAD_REQUEST);
        }

        CustomOAuth2User customUserDetail = (CustomOAuth2User) authentication.getPrincipal();
        String username = customUserDetail.getName();
        UserEntity userEntity = userRepository.findByUsername(username);

        if(userEntity == null) {
            return new ResponseEntity<>("Invalid user", HttpStatus.BAD_REQUEST);
        }
        userEntity.setRole(role);

        userRepository.save(userEntity);
        return new ResponseEntity<>("Save role complete", HttpStatus.OK);
    }
}
