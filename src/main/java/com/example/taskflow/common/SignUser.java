/*
package com.example.taskflow.common;

import com.example.taskflow.security.enums.UserRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "users")
public class SignUser extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private Long userId;

    private String email;

    private String password;

    private String userName;

    private String name;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    public SignUser(String email, String password, String userName, String name) {
        this.email = email;
        this.password = password;
        this.userName = userName;
        this.name = name;
    }
}
*/
