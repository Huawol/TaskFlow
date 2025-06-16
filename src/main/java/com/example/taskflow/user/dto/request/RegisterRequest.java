package com.example.taskflow.user.dto.request;

import lombok.Getter;
import lombok.Setter;

//회원가입 요청 DTO
@Getter
@Setter
public class RegisterRequest {
    private String username;
    private String email;
    private String password;
    private String name;
}
