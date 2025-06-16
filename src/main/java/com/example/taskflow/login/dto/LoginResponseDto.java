package com.example.taskflow.login.dto;

import com.example.taskflow.common.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class LoginResponseDto {

    private final Long id;
    private final String username;
    private final String email;
    private final String name;
    private final LocalDateTime createdAt;

    public LoginResponseDto(User user) {
        this.id = user.getUserId();
        this.username = user.getUserName();
        this.email = user.getEmail();
        this.name = user.getName();
        this.createdAt = user.getCreatedAt();
    }
}
