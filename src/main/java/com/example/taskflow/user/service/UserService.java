package com.example.taskflow.user.service;

import com.example.taskflow.user.dto.LoginRequestDto;
import com.example.taskflow.user.dto.LoginResponseDto;
import com.example.taskflow.user.dto.SignupRequestDto;
import com.example.taskflow.user.dto.SignupResponseDto;

//사용자 서비스 인터페이스
public interface UserService {

	void deleteUser(String token, String email, String password);

	SignupResponseDto signUp(SignupRequestDto signupRequestDto);

	LoginResponseDto login(LoginRequestDto loginRequestDto);

	void logout(String token);

}
