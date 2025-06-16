package com.example.taskflow.user.entity;

import com.example.taskflow.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;  // PK: 사용자 ID

    @Column(nullable = false, unique = true, length = 30)
    private String username;  // 계정명 (unique)

    @Column(nullable = false, unique = true, length = 255)
    private String email;  // 이메일 (unique)

    @Column(nullable = false)
    private String password;  // 암호화된 비밀번호

    @Column(nullable = false, length = 30)
    private String name;  // 사용자 이름

    @Column(nullable = false, length = 10)
    private String role;  // 사용자 역할 (USER / ADMIN)
}
