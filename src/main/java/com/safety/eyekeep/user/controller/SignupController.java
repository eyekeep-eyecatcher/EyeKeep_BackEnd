package com.safety.eyekeep.user.controller;

import com.safety.eyekeep.user.dto.JoinDTO;
import com.safety.eyekeep.user.service.SignupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class SignupController {

    private final SignupService signupService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody JoinDTO joinDto){
        if(signupService.checkLoginIdDuplicate(joinDto.getUsername())){
            return new ResponseEntity<>("Email already in use", HttpStatus.BAD_REQUEST);
        }

        if(signupService.checkNameDuplicate(joinDto.getName())){
            return new ResponseEntity<>("Name already in use", HttpStatus.BAD_REQUEST);
        }

        if(!signupService.checkPasswordRight(joinDto.getPassword(), joinDto.getPasswordCheck())){
            return new ResponseEntity<>("Password does not match", HttpStatus.BAD_REQUEST);
        }

        signupService.join(joinDto);
        return new ResponseEntity<>("Signup Successful", HttpStatus.OK);
    }
}