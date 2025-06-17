package com.example.taskflow.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class LoginRequestDto {

    @NotNull
    private String userName;

    @NotBlank
    private String password;
}
