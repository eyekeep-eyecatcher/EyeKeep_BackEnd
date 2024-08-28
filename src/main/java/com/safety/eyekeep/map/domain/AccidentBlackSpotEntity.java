package com.safety.eyekeep.map.domain;

import com.safety.eyekeep.map.dto.AccidentBlackSpotDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "Accident_Black_Spot")
public class AccidentBlackSpotEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String location;

    @Column
    private String latitude;

    @Column
    private String longitude;

    @Column
    private String accidentType;

    public AccidentBlackSpotDTO toDTO(){
        AccidentBlackSpotDTO accidentBlackSpotDTO = new AccidentBlackSpotDTO();
        accidentBlackSpotDTO.setLocation(null);
        accidentBlackSpotDTO.setLatitude(this.latitude);
        accidentBlackSpotDTO.setLongitude(this.longitude);
        accidentBlackSpotDTO.setAccidentType(this.accidentType);
        return accidentBlackSpotDTO;
    }
}

