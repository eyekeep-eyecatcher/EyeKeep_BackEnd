package com.safety.eyekeep.map.repository;

import com.safety.eyekeep.map.domain.CCTVEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CCTVRepository  extends JpaRepository<CCTVEntity, Long> {
}
