package com.safety.eyekeep.map.domain;


import com.safety.eyekeep.map.dto.SecurityLightDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table
public class SecurityLightEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String address;

    @Column
    private String latitude;

    @Column
    private String longitude;

    public SecurityLightDTO toDTO(){
        SecurityLightDTO securityLightDTO = new SecurityLightDTO();
        securityLightDTO.setAddress(null);
        securityLightDTO.setLatitude(this.latitude);
        securityLightDTO.setLongitude(this.longitude);
        return securityLightDTO;
    }
}
