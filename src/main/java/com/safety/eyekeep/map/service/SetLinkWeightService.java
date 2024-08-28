package com.safety.eyekeep.map.service;

import com.safety.eyekeep.map.domain.LinkList;
import com.safety.eyekeep.map.domain.RoadNetworkLinkEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class SetLinkWeightService {
    private final RepositoryService repositoryService;

    public void setWeightByPoliceOffice() {
        List<RoadNetworkLinkEntity> roadNetworkLinkEntities = repositoryService.findAllRoadNetworkLinkRepositoryToEntity();
        for (RoadNetworkLinkEntity roadNetworkLinkEntity : roadNetworkLinkEntities) {
            List<LinkList> linkList = roadNetworkLinkEntity.getLink();
            LinkList startCoordinate = linkList.get(0);
            LinkList endCoordinate = linkList.get(linkList.size() - 1);
            double majorAxisLength = RouteFinder.calculateDistance(startCoordinate.getLatitude(), startCoordinate.getLongitude(),
                    endCoordinate.getLatitude(), endCoordinate.getLongitude()) * 1.11;
            Double count = repositoryService.countPoliceOfficeRepositoryWithinEllipse(startCoordinate.getLatitude(), startCoordinate.getLongitude(),
                                 endCoordinate.getLatitude(), endCoordinate.getLongitude(), majorAxisLength) * 10;
            roadNetworkLinkEntity.setSafetyWeight(count);
        }
        repositoryService.saveAllRoadNetworkLinkRepositoryToEntity(roadNetworkLinkEntities);
    }
}
