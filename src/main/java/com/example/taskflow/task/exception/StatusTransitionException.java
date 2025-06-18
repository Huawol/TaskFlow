package com.example.taskflow.task.exception;

public class StatusTransitionException extends RuntimeException {
	public StatusTransitionException(String message) {
		super(message);
	}
}
