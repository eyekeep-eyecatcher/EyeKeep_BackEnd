package com.safety.eyekeep.user.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "USER_INFO")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /**
     * email
     */
    @Column(unique = true, name = "USER_NAME")
    private String username;

    @Column(name = "PASSWORD")
    private String password;

    @Column(unique = true, name = "NICKNAME")
    private String nickname;

    @Column(name = "ROLE")
    private String role;

    @Column
    private String fcmToken;

    @Column
    private String family;
}
