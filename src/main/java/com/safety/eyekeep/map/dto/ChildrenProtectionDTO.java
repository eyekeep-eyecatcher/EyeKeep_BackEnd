package com.safety.eyekeep.map.dto;

import com.safety.eyekeep.map.domain.ChildrenProtectionEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChildrenProtectionDTO {
    private String name;
    private String latitude;
    private String longitude;

    public ChildrenProtectionEntity toEntity(){
        ChildrenProtectionEntity childrenProtectionEntity = new ChildrenProtectionEntity();
        childrenProtectionEntity.setName(this.name);
        childrenProtectionEntity.setLatitude(this.latitude);
        childrenProtectionEntity.setLongitude(this.longitude);
        return childrenProtectionEntity;
    }
}
