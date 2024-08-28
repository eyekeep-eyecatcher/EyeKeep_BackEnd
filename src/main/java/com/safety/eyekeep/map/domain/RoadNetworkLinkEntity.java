package com.safety.eyekeep.map.domain;

import com.safety.eyekeep.map.dto.RoadNetworkLinkDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "Road_Network_Link")
public class RoadNetworkLinkEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String linkId;

    @Column
    @ElementCollection
    @CollectionTable(name = "Link_List", joinColumns = @JoinColumn(name = "road_network_link_id"))
    private List<LinkList> link;

    @Column
    private String length;

    @Column
    private String startNode;

    @Column
    private String endNode;

    @Column
    private Double safetyWeight;

    public RoadNetworkLinkDTO toDTO(){
        RoadNetworkLinkDTO roadNetworkLinkDTO = new RoadNetworkLinkDTO();
        roadNetworkLinkDTO.setLinkId(this.linkId);
        roadNetworkLinkDTO.setLink(this.link);
        roadNetworkLinkDTO.setLength(this.length);
        roadNetworkLinkDTO.setStartNode(this.startNode);
        roadNetworkLinkDTO.setEndNode(this.endNode);
        roadNetworkLinkDTO.setSafetyWeight(this.safetyWeight);
        return roadNetworkLinkDTO;
    }
}


