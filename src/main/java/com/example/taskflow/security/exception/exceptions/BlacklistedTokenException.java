package com.example.taskflow.security.exception.exceptions;

public class BlacklistedTokenException extends RuntimeException {

	public BlacklistedTokenException(String message) {
		super(message);
	}

}