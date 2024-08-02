package com.safety.eyekeep.user.dto;

import com.safety.eyekeep.user.domain.UserEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JoinDTO {

    /**
     * email
     */
    private String username;
    private String password;
    private String passwordCheck;
    private String name;
    private String role;

    // password encryption O
    public UserEntity toEntity(String encodedPassword){
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(this.username);
        userEntity.setPassword(encodedPassword);
        userEntity.setName(this.name);
        userEntity.setRole(this.role);
        return userEntity;
    }
}
