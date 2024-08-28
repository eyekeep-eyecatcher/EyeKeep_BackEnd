package com.safety.eyekeep.user.repository;

import com.safety.eyekeep.user.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    boolean existsByUsername(String username);
    boolean existsByNickname(String name);
    UserEntity findByUsername(String username);
    UserEntity findByNickname(String nickname);
}