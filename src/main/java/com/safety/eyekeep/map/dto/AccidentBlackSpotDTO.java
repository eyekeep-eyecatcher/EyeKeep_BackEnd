package com.safety.eyekeep.map.dto;

import com.safety.eyekeep.map.domain.AccidentBlackSpotEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccidentBlackSpotDTO {
    private String location;
    private String latitude;
    private String longitude;
    private String accidentType;

    public AccidentBlackSpotEntity toEntity(){
        AccidentBlackSpotEntity accidentBlackSpotEntity = new AccidentBlackSpotEntity();
        accidentBlackSpotEntity.setLocation(this.location);
        accidentBlackSpotEntity.setLatitude(this.latitude);
        accidentBlackSpotEntity.setLongitude(this.longitude);
        accidentBlackSpotEntity.setAccidentType(this.accidentType);
        return accidentBlackSpotEntity;
    }
}
