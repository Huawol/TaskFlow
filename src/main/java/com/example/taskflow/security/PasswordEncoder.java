package com.example.taskflow.security;

import org.springframework.stereotype.Component;

import at.favre.lib.crypto.bcrypt.BCrypt;

//Bcrypt를 이용한 비밀번호 암호화 및 검증
@Component
public class PasswordEncoder {

	public String encode(String rawPassword) {
		return BCrypt.withDefaults().hashToString(BCrypt.MIN_COST, rawPassword.toCharArray());
	}

	public boolean matches(String rawPassword, String encodedPassword) {
		return BCrypt.verifyer().verify(rawPassword.toCharArray(), encodedPassword).verified;
	}

}