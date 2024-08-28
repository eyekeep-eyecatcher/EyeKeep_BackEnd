package com.safety.eyekeep.map.dto;

import com.safety.eyekeep.map.domain.SafetyEmergencyBellEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SafetyEmergencyBellDTO {
    private String location;
    private String latitude;
    private String longitude;

    public SafetyEmergencyBellEntity toEntity(){
        SafetyEmergencyBellEntity safetyEmergencyBellEntity = new SafetyEmergencyBellEntity();
        safetyEmergencyBellEntity.setLocation(this.location);
        safetyEmergencyBellEntity.setLatitude(this.latitude);
        safetyEmergencyBellEntity.setLongitude(this.longitude);
        return safetyEmergencyBellEntity;
    }
}
