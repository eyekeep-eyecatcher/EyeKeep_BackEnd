package com.safety.eyekeep.map.dto;


import com.safety.eyekeep.map.domain.SecurityLightEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SecurityLightDTO {

    private String address;
    private String latitude;
    private String longitude;

    public SecurityLightEntity toEntity(){
        SecurityLightEntity securityLightEntity = new SecurityLightEntity();
        securityLightEntity.setAddress(this.address);
        securityLightEntity.setLatitude(this.latitude);
        securityLightEntity.setLongitude(this.longitude);
        return securityLightEntity;
    }
}
