package com.safety.eyekeep.map.repository;

import com.safety.eyekeep.map.domain.AccidentBlackSpotEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccidentBlackSpotRepository extends JpaRepository<AccidentBlackSpotEntity, Long> {
}
