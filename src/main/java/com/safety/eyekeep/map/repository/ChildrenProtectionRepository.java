package com.safety.eyekeep.map.repository;

import com.safety.eyekeep.map.domain.ChildrenProtectionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChildrenProtectionRepository extends JpaRepository<ChildrenProtectionEntity, Long> {
}
