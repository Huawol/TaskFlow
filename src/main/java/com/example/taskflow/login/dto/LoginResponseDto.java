package com.example.taskflow.login.dto;

import com.example.taskflow.common.SignUser;
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

    public LoginResponseDto(SignUser signUser) {
        this.id = signUser.getUserId();
        this.username = signUser.getUserName();
        this.email = signUser.getEmail();
        this.name = signUser.getName();
        this.createdAt = signUser.getCreatedAt();
    }
}
