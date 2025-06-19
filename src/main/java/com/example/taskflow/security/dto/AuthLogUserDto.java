package com.example.taskflow.security.dto;

import com.example.taskflow.security.enums.UserRole;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AuthLogUserDto {

	private Long id;
	private String username;
	private String email;
	private UserRole userRole;

	public AuthLogUserDto(Long id, String username, String email, UserRole userRole) {
		this.id = id;
		this.username = username;
		this.email = email;
		this.userRole = userRole;
	}

}