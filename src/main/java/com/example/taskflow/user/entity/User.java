package com.example.taskflow.user.entity;

import com.example.taskflow.common.BaseEntity;  //BaseEntity 상속을 위해 import 추가
import com.example.taskflow.security.enums.UserRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Entity
@Setter
@Table(name = "users")
public class User extends BaseEntity {  //BaseEntity 상속 추가

    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    private String email;

    private String password;

    private String userName;

    private String name;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    public User( String email, String password, String userName, String name) {
        this.email = email;
        this.password = password;
        this.userName = userName;
        this.name = name;
    }


}