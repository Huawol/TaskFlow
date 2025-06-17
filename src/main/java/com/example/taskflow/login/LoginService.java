package com.example.taskflow.login;

import com.example.taskflow.login.dto.LoginRequestDto;
import com.example.taskflow.login.dto.LoginResponseDto;
import com.example.taskflow.login.dto.SignupRequestDto;
import com.example.taskflow.login.dto.SignupResponseDto;

public interface LoginService {

    SignupResponseDto signUp(SignupRequestDto signupRequestDto);
    LoginResponseDto login(LoginRequestDto loginRequestDto);
}
