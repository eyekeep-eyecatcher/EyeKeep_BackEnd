package com.safety.eyekeep.map.domain;

import com.safety.eyekeep.map.dto.ChildrenGuardHouseDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "Children_Guard_House")
public class ChildrenGuardHouseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String name;

    @Column
    private String latitude;

    @Column
    private String longitude;

    public ChildrenGuardHouseDTO toDTO(){
        ChildrenGuardHouseDTO childrenGuardHouseDTO = new ChildrenGuardHouseDTO();
        childrenGuardHouseDTO.setName(null);
        childrenGuardHouseDTO.setLatitude(this.latitude);
        childrenGuardHouseDTO.setLongitude(this.longitude);
        return childrenGuardHouseDTO;
    }
}

