package com.example.taskflow.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class SignupRequestDto {

	@NotNull
	private String email;

	@NotNull
	private String password;

	@NotBlank
	private String userName;

	@NotNull
	private String name;
}