package com.safety.eyekeep.map.dto;

import com.safety.eyekeep.map.domain.RoadNetworkLinkEntity;
import com.safety.eyekeep.map.domain.LinkList;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RoadNetworkLinkDTO {
    private Long id;
    private String linkId;
    private List<LinkList> link;
    private String length;
    private String startNode;
    private String endNode;
    private Double safetyWeight;

    public RoadNetworkLinkEntity toEntity(){
        RoadNetworkLinkEntity roadNetworkLinkEntity = new RoadNetworkLinkEntity();
        roadNetworkLinkEntity.setLinkId(this.linkId);
        roadNetworkLinkEntity.setLink(this.link);
        roadNetworkLinkEntity.setLength(this.length);
        roadNetworkLinkEntity.setStartNode(this.startNode);
        roadNetworkLinkEntity.setEndNode(this.endNode);
        roadNetworkLinkEntity.setSafetyWeight(this.safetyWeight);
        return roadNetworkLinkEntity;
    }
}
