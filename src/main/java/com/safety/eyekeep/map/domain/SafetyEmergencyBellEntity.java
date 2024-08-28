package com.safety.eyekeep.map.domain;

import com.safety.eyekeep.map.dto.SafetyEmergencyBellDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "Safety_Emergency_Bell")
public class SafetyEmergencyBellEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String location;

    @Column
    private String latitude;

    @Column
    private String longitude;


    public SafetyEmergencyBellDTO toDTO(){
        SafetyEmergencyBellDTO safetyEmergencyBellDTO = new SafetyEmergencyBellDTO();
        safetyEmergencyBellDTO.setLocation(null);
        safetyEmergencyBellDTO.setLatitude(this.latitude);
        safetyEmergencyBellDTO.setLongitude(this.longitude);
        return safetyEmergencyBellDTO;
    }
}

