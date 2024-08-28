package com.safety.eyekeep.user.controller;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.safety.eyekeep.user.domain.CustomUserDetails;
import com.safety.eyekeep.user.domain.UserEntity;
import com.safety.eyekeep.user.service.SendMessageService;
import com.safety.eyekeep.user.service.SignupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;


@RestController
@RequiredArgsConstructor
public class FCMController {
    private final SignupService signupService;
    private final SendMessageService sendMessageService;

    @PostMapping("/fcm/token")
    public ResponseEntity<?> saveFCMToken(@RequestParam("fcmToken") String fcmToken, Authentication authentication) {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        String username = customUserDetails.getUsername();
        UserEntity userEntity = signupService.findByUsername(username);
        if (userEntity == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        userEntity.setFcmToken(fcmToken);
        signupService.save(userEntity);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/fcm/request")
    public ResponseEntity<?> requestChild(@RequestParam("email") String childEmail, Authentication authentication) throws FirebaseMessagingException {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        String username = customUserDetails.getUsername();
        UserEntity parentEntity = signupService.findByUsername(username);
        if (parentEntity == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        // 부모가 이미 아이킵 등록이 되어 있을 경우 403 error 메세지
        String child = parentEntity.getFamily();
        if(child != null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        String nickname = parentEntity.getNickname();

        UserEntity childEntity = signupService.findByUsername(childEmail) != null ? signupService.findByUsername(childEmail) : null;
        if (childEntity == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        // 아이가 이미 아이킵 등록이 되어있을 경우 403 error 메세지
        String parent = childEntity.getFamily();
        if(parent != null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        String fcmToken = childEntity.getFcmToken();
        if (fcmToken == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        String response = sendMessageService.sendEyeKeepMessage(fcmToken, username, nickname);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/fcm/accept")
    public ResponseEntity<?> registerChild(@RequestParam("email") String email, Authentication authentication) {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        String username = customUserDetails.getUsername();
        UserEntity childEntity = signupService.findByUsername(username);
        if (childEntity == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        // 아이가 이미 아이킵 등록이 되어있을 경우 403 error 메세지
        String parent = childEntity.getFamily();
        if(parent != null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        UserEntity parentEntity = signupService.findByUsername(email) != null ? signupService.findByUsername(email) : null;
        if (parentEntity == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        // 부모가 이미 아이킵 등록이 되어 있을 경우 403 error 메세지
        String child = parentEntity.getFamily();
        if(child != null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        String childEmail = childEntity.getUsername();
        String parentEmail = parentEntity.getUsername();

        childEntity.setFamily(parentEmail);
        signupService.save(childEntity);


        parentEntity.setFamily(childEmail);
        signupService.save(parentEntity);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/fcm/emergency")
    public ResponseEntity<?> emergency(Authentication authentication) throws FirebaseMessagingException {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        String username = customUserDetails.getUsername();
        UserEntity childEntity = signupService.findByUsername(username);
        if (childEntity == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        String parent = childEntity.getFamily();
        if(parent == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }


        String nickname = childEntity.getNickname();
        String fcmToken = signupService.findByUsername(parent).getFcmToken();
        if (fcmToken == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        String response = sendMessageService.sendEmergencyMessage(fcmToken, nickname);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
