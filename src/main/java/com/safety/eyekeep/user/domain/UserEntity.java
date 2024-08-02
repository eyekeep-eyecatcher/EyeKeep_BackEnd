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

    @Column(unique = true, name = "NAME")
    private String name;

    @Column(name = "ROLE")
    private String role;
}
