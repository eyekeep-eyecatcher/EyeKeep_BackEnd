package com.safety.eyekeep.map.repository;

import com.safety.eyekeep.map.domain.RoadNetworkLinkEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoadNetworkLinkRepository extends JpaRepository<RoadNetworkLinkEntity, Long> {
    @Query(value = "SELECT rnl.* " +
            "FROM Road_Network_Link rnl " +
            "JOIN Link_List ll ON rnl.id = ll.road_network_link_id " +
            "GROUP BY rnl.id " +
            "HAVING COUNT(*) = " +
            "  (SELECT COUNT(*) FROM Link_List ll2 " +
            "   WHERE ll2.road_network_link_id = rnl.id " +
            "     AND POW(ll2.latitude - :centerLatitude, 2) + POW(ll2.longitude - :centerLongitude, 2) <= POW(:radius, 2))",
            nativeQuery = true)
    List<RoadNetworkLinkEntity> findLinksWithInRadius(@Param("centerLatitude") double centerLatitude,
                                                      @Param("centerLongitude") double centerLongitude,
                                                      @Param("radius") double radius);

}
