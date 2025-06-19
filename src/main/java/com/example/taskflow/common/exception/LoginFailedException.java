package com.example.taskflow.common.exception;

public class LoginFailedException extends RuntimeException {

	public LoginFailedException(String message) {
		super(message);
	}

}