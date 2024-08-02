package com.safety.eyekeep.user.service;

import com.safety.eyekeep.user.domain.CustomUserDetails;
import com.safety.eyekeep.user.domain.UserEntity;
import com.safety.eyekeep.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
// JPA를 이용하여 DB에서 유저 정보를 조회하는 class
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByUsername(username);
        if (userEntity == null) {
            throw new UsernameNotFoundException("Invalid authentication!");
        }

        return new CustomUserDetails(userEntity);
    }
}