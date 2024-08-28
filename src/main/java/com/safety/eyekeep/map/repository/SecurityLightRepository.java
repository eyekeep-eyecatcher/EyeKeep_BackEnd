package com.safety.eyekeep.map.repository;

import com.safety.eyekeep.map.domain.SecurityLightEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SecurityLightRepository extends JpaRepository<SecurityLightEntity, Long> {
}
