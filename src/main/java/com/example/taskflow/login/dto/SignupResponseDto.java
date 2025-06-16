package com.example.taskflow.login.dto;

import com.example.taskflow.common.User;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class SignupResponseDto {

    private final Long id;
    private final String email;
    private final String name;
    private final LocalDateTime createdAt;

    public SignupResponseDto(User user) {
        this.id = user.getUserId();
        this.email = user.getEmail();
        this.name = user.getUserName();
        this.createdAt = user.getCreatedAt();
    }
}
