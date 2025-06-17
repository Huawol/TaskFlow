package com.example.taskflow.login.dto;

import com.example.taskflow.common.SignUser;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class SignupResponseDto {

    private final Long id;
    private final String email;
    private final String name;
    private final LocalDateTime createdAt;

    public SignupResponseDto(SignUser signUser) {
        this.id = signUser.getUserId();
        this.email = signUser.getEmail();
        this.name = signUser.getUserName();
        this.createdAt = signUser.getCreatedAt();
    }
}
