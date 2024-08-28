package com.safety.eyekeep.map.domain;

import com.safety.eyekeep.map.dto.ChildrenProtectionDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "Children_Protection")
public class ChildrenProtectionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String name;

    @Column
    private String latitude;

    @Column
    private String longitude;

    public ChildrenProtectionDTO toDTO(){
        ChildrenProtectionDTO childrenProtectionDTO = new ChildrenProtectionDTO();
        childrenProtectionDTO.setName(null);
        childrenProtectionDTO.setLatitude(this.latitude);
        childrenProtectionDTO.setLongitude(this.longitude);
        return childrenProtectionDTO;
    }
}
