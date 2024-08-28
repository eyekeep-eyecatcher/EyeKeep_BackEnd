package com.safety.eyekeep.map.repository;

import com.safety.eyekeep.map.domain.PoliceOfficeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PoliceOfficeRepository  extends JpaRepository<PoliceOfficeEntity, Long> {
    @Query(value = "SELECT COUNT(*) FROM Police_Office p WHERE " +
            "(SQRT(POWER(:latitude1 - CAST(p.latitude AS double), 2) + POWER(:longitude1 - CAST(p.longitude AS double), 2)) + " +
            "SQRT(POWER(:latitude2 - CAST(p.latitude AS double), 2) + POWER(:longitude2 - CAST(p.longitude AS double), 2))) <= :majorAxisLength",
            nativeQuery = true)
    Double countPoliceOfficesWithinEllipse(
            @Param("latitude1") double latitude1,
            @Param("longitude1") double longitude1,
            @Param("latitude2") double latitude2,
            @Param("longitude2") double longitude2,
            @Param("majorAxisLength") double majorAxisLength);
}
