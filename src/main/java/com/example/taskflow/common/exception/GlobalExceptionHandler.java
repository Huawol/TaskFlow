package com.example.taskflow.common.exception;

import java.util.stream.Collectors;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.taskflow.common.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ExampleException.class)
	public ApiResponse<Void> handlerExampleException(ExampleException ex) {
		return new ApiResponse<>(false, ex.getMessage(), null);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ApiResponse<Void> handlerValidationException(MethodArgumentNotValidException ex) {

		String message = ex.getBindingResult()
			.getFieldErrors()
			.stream()
			.map(DefaultMessageSourceResolvable::getDefaultMessage)
			.collect(Collectors.joining(","));

		return new ApiResponse<>(false, message, null);
	}

}
