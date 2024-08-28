package com.safety.eyekeep.map.controller;

import com.safety.eyekeep.map.save.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class PublicDataAPIController {
    private final SaveSeoulSecurityLightAPI saveSeoulSecurityLightAPI;
    private final SaveDongdaemunSecurityLightAPI saveDongdaemunSecurityLightAPI;
    private final ReadSeoulSecurityLightCSV readSeoulSecurityLightCSV;
    private final SavePoliceOfficeAPI savePoliceOfficeAPI;
    private final ReadSeoulCCTVCSV readSeoulCCTVCSV;
    private final ReadChildrenProtectionCSV readChildrenProtectionCSV;
    private final ReadRoadNetworkCSV readRoadNetworkCSV;
    private final SaveChildrenGuardHouseAPI saveChildrenGuardHouseAPI;
    private final SaveSafetyEmergencyBellAPI saveSafetyEmergencyBellAPI;
    private final SaveAccidentBlackSpotAPI saveAccidentBlackSpotAPI;

    @PostMapping("/safetydata/save/security/api")
    public ResponseEntity<?> saveSecurityAPI() throws IOException {
        String result = saveSeoulSecurityLightAPI.saveSeoulSecurityLight();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/safetydata/save/dongdaemun")
    public ResponseEntity<?> saveDongdaemun() throws IOException {
        String result = saveDongdaemunSecurityLightAPI.saveDongdaemunSecurityLight();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/safetydata/save/security/csv")
    public ResponseEntity<?> saveSecurityCSV() {
        String result = readSeoulSecurityLightCSV.saveSeoulSecurityLightCSV();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/safetydata/save/police")
    public ResponseEntity<?> savePolice() throws IOException {
        String result = savePoliceOfficeAPI.savePoliceOffice();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/safetydata/save/cctv")
    public ResponseEntity<?> saveCCTV() {
        String result = readSeoulCCTVCSV.saveSeoulCCTVcsv();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/safetydata/save/children/protection")
    public ResponseEntity<?> saveChildrenProtectionZone() {
        String result = readChildrenProtectionCSV.saveChildrenProtectioncsv();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/safetydata/save/roadnetwork")
    public ResponseEntity<?> saveRoadNetwork() {
        String result = readRoadNetworkCSV.saveRoadNetworkcsv();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/safetydata/save/children/guardhouse")
    public ResponseEntity<?> saveChildrenguardhouse() throws IOException {
        String result = saveChildrenGuardHouseAPI.saveChildGuardHouse();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/safetydata/save/safetybell")
    public ResponseEntity<?> saveSafetyEmergencyBell() throws IOException {
        String result = saveSafetyEmergencyBellAPI.saveSafetyEmergencyBell();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/safetydata/save/accident/blackspot")
    public ResponseEntity<?> saveAccidentBlackSpot() throws IOException {
        String result = saveAccidentBlackSpotAPI.saveAccidentBlackSpot();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
