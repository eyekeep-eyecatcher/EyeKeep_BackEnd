package com.safety.eyekeep.map.dto;


import com.safety.eyekeep.map.domain.PoliceOfficeEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PoliceOfficeDTO {

    private String officeName;
    private String latitude;
    private String longitude;

    public PoliceOfficeEntity toEntity(){
        PoliceOfficeEntity policeOfficeEntity = new PoliceOfficeEntity();
        policeOfficeEntity.setOfficeName(this.officeName);
        policeOfficeEntity.setLatitude(this.latitude);
        policeOfficeEntity.setLongitude(this.longitude);
        return policeOfficeEntity;
    }
}
