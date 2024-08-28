package com.safety.eyekeep.map.controller;

import com.safety.eyekeep.map.dto.ChildLocationDTO;
import com.safety.eyekeep.map.service.LocationRedisService;
import com.safety.eyekeep.user.domain.CustomUserDetails;
import com.safety.eyekeep.user.domain.UserEntity;
import com.safety.eyekeep.user.service.SignupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ChildLocationController {
    private final LocationRedisService locationRedisService;
    private final SignupService signupService;

    @PostMapping("/save/location")
    public ResponseEntity<?> saveLocation(@RequestBody ChildLocationDTO childLocationDTO, Authentication authentication) {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        String username = customUserDetails.getUsername();

        if(locationRedisService.checkLocationExists(username)){
            locationRedisService.addLocation(username, childLocationDTO);
        }
        else{
            // TTL을 7일로 설정 (7일 = 7 * 24 * 60 * 60 초)
            Duration ttl = Duration.ofDays(7);
            locationRedisService.setLocation(username, childLocationDTO, ttl);
        }

        return ResponseEntity.ok().build();
    }

    @GetMapping("/request/location/now")
    public ResponseEntity<?> getCurrentLocation(Authentication authentication) {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        String username = customUserDetails.getUsername();
        UserEntity userEntity = signupService.findByUsername(username);
        String child = userEntity.getFamily();
        if(child == null) {
            log.info("Eye Keep is null");
            Map<String, Object> message = new HashMap<>();
            message.put("HttpForBiddenError", "Eye keep isn't set");
            return new ResponseEntity<>(message, HttpStatus.FORBIDDEN);
        }

        // 아이 위치가 redis에 존재하지 않을 경우
        if(!locationRedisService.checkLocationExists(child)){
            log.info("Child location is not found in redis");
            Map<String, Object> message = new HashMap<>();
            message.put("HttpNotFoundError", "Child location is not found");
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }

        ChildLocationDTO childLocationDTO = locationRedisService.getLastLocation(child);
        return new ResponseEntity<>(childLocationDTO, HttpStatus.OK);
    }

    @GetMapping("/request/location/all")
    public ResponseEntity<?> getAllLocations(Authentication authentication) {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        String username = customUserDetails.getUsername();
        UserEntity userEntity = signupService.findByUsername(username);
        String child = userEntity.getFamily();
        if(child == null) {
            log.info("Eye Keep is null");
            Map<String, Object> message = new HashMap<>();
            message.put("HttpForBiddenError", "Eye keep isn't set");
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }

        // 아이 위치가 redis에 존재하지 않을 경우
        if(!locationRedisService.checkLocationExists(child)){
            log.info("Child location is not found in redis");
            Map<String, Object> message = new HashMap<>();
            message.put("HttpNotFoundError", "Child location is not found");
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }

        List<ChildLocationDTO> childLocationDTOs = locationRedisService.getAllLocations(child);
        return new ResponseEntity<>(childLocationDTOs, HttpStatus.OK);
    }
}
