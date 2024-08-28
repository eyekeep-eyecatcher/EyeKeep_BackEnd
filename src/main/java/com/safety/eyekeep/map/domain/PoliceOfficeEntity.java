package com.safety.eyekeep.map.domain;

import com.safety.eyekeep.map.dto.PoliceOfficeDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "Police_Office")
public class PoliceOfficeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String officeName;

    @Column
    private String latitude;

    @Column
    private String longitude;

    public PoliceOfficeDTO toDTO(){
        PoliceOfficeDTO policeOfficeDTO = new PoliceOfficeDTO();
        policeOfficeDTO.setOfficeName(this.officeName);
        policeOfficeDTO.setLatitude(this.latitude);
        policeOfficeDTO.setLongitude(this.longitude);
        return policeOfficeDTO;
    }
}
