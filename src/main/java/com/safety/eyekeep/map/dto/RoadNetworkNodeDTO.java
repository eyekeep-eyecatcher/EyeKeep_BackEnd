package com.safety.eyekeep.map.dto;

import com.safety.eyekeep.map.domain.RoadNetworkNodeEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoadNetworkNodeDTO {
    private String nodeId;
    private double latitude;
    private double longitude;

    public RoadNetworkNodeEntity toEntity(){
        RoadNetworkNodeEntity roadNetworkNodeEntity = new RoadNetworkNodeEntity();
        roadNetworkNodeEntity.setNodeId(this.nodeId);
        roadNetworkNodeEntity.setLatitude(this.latitude);
        roadNetworkNodeEntity.setLongitude(this.longitude);
        return roadNetworkNodeEntity;
    }
}
