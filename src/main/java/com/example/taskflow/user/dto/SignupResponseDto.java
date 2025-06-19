package com.example.taskflow.user.dto;

import java.time.LocalDateTime;

import com.example.taskflow.user.entity.User;

import lombok.Getter;

@Getter
public class SignupResponseDto {

	private final Long id;
	private final String username;
	private final String email;
	private final String name;
	private final String role;
	private final LocalDateTime createdAt;

	public SignupResponseDto(User user) {
		this.id = user.getId();
		this.username = user.getUsername();
		this.email = user.getEmail();
		this.name = user.getName();
		this.role = user.getRole().name();
		this.createdAt = user.getCreatedAt();
	}

}