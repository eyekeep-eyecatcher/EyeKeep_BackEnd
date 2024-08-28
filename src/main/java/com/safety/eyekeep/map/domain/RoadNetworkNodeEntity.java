package com.safety.eyekeep.map.domain;

import com.safety.eyekeep.map.dto.RoadNetworkNodeDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table
public class RoadNetworkNodeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String nodeId;

    @Column
    private double latitude;

    @Column
    private double longitude;

    public RoadNetworkNodeDTO toDTO(){
        RoadNetworkNodeDTO roadNetworkNodeDTO = new RoadNetworkNodeDTO();
        roadNetworkNodeDTO.setNodeId(this.nodeId);
        roadNetworkNodeDTO.setLatitude(this.latitude);
        roadNetworkNodeDTO.setLongitude(this.longitude);
        return roadNetworkNodeDTO;
    }
}
