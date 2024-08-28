package com.safety.eyekeep.map.dto;

import com.safety.eyekeep.map.domain.ChildrenGuardHouseEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChildrenGuardHouseDTO {
    private String name;
    private String latitude;
    private String longitude;

    public ChildrenGuardHouseEntity toEntity(){
        ChildrenGuardHouseEntity childrenGuardHouseEntity = new ChildrenGuardHouseEntity();
        childrenGuardHouseEntity.setName(this.name);
        childrenGuardHouseEntity.setLatitude(this.latitude);
        childrenGuardHouseEntity.setLongitude(this.longitude);
        return childrenGuardHouseEntity;
    }
}
