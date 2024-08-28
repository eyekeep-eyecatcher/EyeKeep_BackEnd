package com.safety.eyekeep.map.repository;

import com.safety.eyekeep.map.domain.RoadNetworkNodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoadNetworkNodeRepository extends JpaRepository<RoadNetworkNodeEntity, Long> {
    @Query(value = "SELECT * FROM road_network_node " +
            "WHERE POW(latitude - :centerLatitude, 2) + POW(longitude - :centerLongitude, 2) <= POW(:radius, 2)",
            nativeQuery = true)
    List<RoadNetworkNodeEntity> findNodesWithInRadius(@Param("centerLatitude") double centerLatitude,
                                                      @Param("centerLongitude") double centerLongitude,
                                                      @Param("radius") double radius);
}
