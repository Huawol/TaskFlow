package com.example.taskflow.user.dto;

import java.time.LocalDateTime;

import com.example.taskflow.user.entity.User;

import lombok.Getter;

@Getter
public class SignupResponseDto {

	private final Long id;
	private final String email;
	private final String name;
	private final LocalDateTime createdAt;

	public SignupResponseDto(User user) {
		this.id = user.getId();
		this.email = user.getEmail();
		this.name = user.getUserName();
		this.createdAt = user.getCreatedAt();
	}

}