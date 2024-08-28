package com.safety.eyekeep.map.dto;

import com.safety.eyekeep.map.domain.CCTVEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CCTVDTO {
    private String address;
    private String latitude;
    private String longitude;

    public CCTVEntity toEntity(){
        CCTVEntity cctvEntity = new CCTVEntity();
        cctvEntity.setAddress(this.address);
        cctvEntity.setLatitude(this.latitude);
        cctvEntity.setLongitude(this.longitude);
        return cctvEntity;
    }
}
