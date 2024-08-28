package com.safety.eyekeep.map.controller;

import com.safety.eyekeep.map.dto.*;
import com.safety.eyekeep.map.service.RepositoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class RequestPublicDataController {
    private final RepositoryService repositoryService;

    @GetMapping("/request/police")
    public ResponseEntity<?> requestPoliceOffice() {
        List<PoliceOfficeDTO> policeOfficeDTOs = repositoryService.findAllPoliceOfficeRepository();
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("data", policeOfficeDTOs);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @GetMapping("/request/security")
    public ResponseEntity<?> requestSecurity() {
        List<SecurityLightDTO> securityLightDTOs = repositoryService.findAllSecurityLightRepository();
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("data", securityLightDTOs);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @GetMapping("/request/cctv")
    public ResponseEntity<?> requestCCTV() {
        List<CCTVDTO> cctvDTOs = repositoryService.findAllCCTVRepository();
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("data", cctvDTOs);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @GetMapping("/request/children/protection")
    public ResponseEntity<?> requestChildren() {
        List<ChildrenProtectionDTO> childrenProtectionDTOs = repositoryService.findAllChildrenProtectRepository();
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("data", childrenProtectionDTOs);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @GetMapping("/request/children/guardhouse")
    public ResponseEntity<?> requestGuardhouse() {
        List<ChildrenGuardHouseDTO> childrenGuardHouseDTOs = repositoryService.findAllChildrenGuardHouseRepository();
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("data", childrenGuardHouseDTOs);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @GetMapping("/request/safetybell")
    public ResponseEntity<?> requestSafetyBell() {
        List<SafetyEmergencyBellDTO> safetyEmergencyBellDTOs = repositoryService.findAllSafetyEmergencyBellRepository();
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("data", safetyEmergencyBellDTOs);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @GetMapping("/request/accident/blackspot")
    public ResponseEntity<?> requestAccidentBlackspot() {
        List<AccidentBlackSpotDTO> accidentBlackSpotDTOs = repositoryService.findAllAccidentBlackSpotRepository();
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("data", accidentBlackSpotDTOs);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

}
