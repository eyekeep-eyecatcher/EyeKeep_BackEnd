package com.safety.eyekeep.user.service;

import com.safety.eyekeep.user.domain.UserEntity;
import com.safety.eyekeep.user.dto.JoinDTO;
import com.safety.eyekeep.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class SignupService {

    private final UserRepository userRepository;

    // Spring Security를 사용한 로그인 구현 시 사용
    private final PasswordEncoder encoder;

    /**
     * loginId 중복 체크
     * 중복되면 true return
     */
    public boolean checkLoginIdDuplicate(String loginId) {
        return userRepository.existsByUsername(loginId);
    }

    /**
     * nickname 중복 체크
     * 중복되면 true return
     */
    public boolean checkNameDuplicate(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    /**
     * password == passwordCheck 확인.
     * 같으면 true return.
     */
    public boolean checkPasswordRight(String password, String passwordCheck){
        return password.equals(passwordCheck);
    }

    /**
     * @param username
     * @return UserEntity
     */
    public UserEntity findByUsername(String username) { return userRepository.findByUsername(username); }

    /**
     * @param nickname
     * @return UserEntity
     */
    public UserEntity findByNickname(String nickname) { return userRepository.findByNickname(nickname); }

    /**
     * 회원가입 기능
     * 화면에서 JoinDto(loginId, password, nickname, role)을 입력받아 User로 변환 후 저장
     * 비밀번호를 암호화해서 저장
     * loginId, nickname 중복 체크는 Controller에서 진행 => 에러 메세지 출력을 위해
     */
    public void join(JoinDTO dto) {
        userRepository.save(dto.toEntity(encoder.encode(dto.getPassword())));
    }

    public void save(UserEntity user) {
        userRepository.save(user);
    }

}
