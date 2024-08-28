package com.safety.eyekeep.map.repository;

import com.safety.eyekeep.map.domain.SafetyEmergencyBellEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SafetyEmergencyBellRepository extends JpaRepository<SafetyEmergencyBellEntity, Long> {
}
