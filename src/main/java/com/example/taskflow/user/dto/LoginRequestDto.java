package com.example.taskflow.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class LoginRequestDto {

	@NotBlank(message = "아이디를 입력해주세요.")
	private String userName;

	@NotBlank(message = "비밀번호를 입력해주세요")
	private String password;
}