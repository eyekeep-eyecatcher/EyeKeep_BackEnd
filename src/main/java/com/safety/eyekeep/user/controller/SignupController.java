package com.safety.eyekeep.user.controller;

import com.safety.eyekeep.user.dto.JoinDTO;
import com.safety.eyekeep.user.service.SignupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class SignupController {

    private final SignupService signupService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody JoinDTO joinDto){
        if(signupService.checkLoginIdDuplicate(joinDto.getUsername())){
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Email");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        if(signupService.checkNameDuplicate(joinDto.getNickname())){
            Map<String, Object> response = new HashMap<>();
            response.put("message", "NickName");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        if(!signupService.checkPasswordRight(joinDto.getPassword(), joinDto.getPasswordCheck())){
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Password");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        signupService.join(joinDto);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Success");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}