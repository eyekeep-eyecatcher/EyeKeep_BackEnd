package com.safety.eyekeep.map.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NodeToNode {
    private RoadNetworkNodeDTO fromNode;
    private RoadNetworkNodeDTO toNode;
    private RoadNetworkLinkDTO edge;
}
