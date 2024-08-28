package com.safety.eyekeep.map.repository;

import com.safety.eyekeep.map.domain.ChildrenGuardHouseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChildrenGuardHouseRepository extends JpaRepository<ChildrenGuardHouseEntity, Long> {
}
