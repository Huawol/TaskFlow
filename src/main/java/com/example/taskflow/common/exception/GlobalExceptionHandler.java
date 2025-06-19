package com.example.taskflow.common.exception;

import java.util.stream.Collectors;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.taskflow.common.ApiResponse;
import com.example.taskflow.security.exception.exceptions.BlacklistedTokenException;
import com.example.taskflow.task.exception.PriorityTransitionException;
import com.example.taskflow.task.exception.StatusTransitionException;
import com.sun.jdi.request.DuplicateRequestException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ExampleException.class)
	public ResponseEntity<ApiResponse<Void>> handlerExampleException(ExampleException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(false, ex.getMessage(), null));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiResponse<Void>> handlerValidationException(MethodArgumentNotValidException ex) {

		String message = ex.getBindingResult()
			.getFieldErrors()
			.stream()
			.map(DefaultMessageSourceResolvable::getDefaultMessage)
			.collect(Collectors.joining(","));

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(false, message, null));
	}

	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<ApiResponse<Void>> handlerUserNotFoundException(UserNotFoundException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, ex.getMessage(), null));
	}

	@ExceptionHandler(StatusTransitionException.class)
	public ResponseEntity<ApiResponse<Void>> handlerStatusTransitionException(StatusTransitionException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(false, ex.getMessage(), null));
	}

	@ExceptionHandler(PriorityTransitionException.class)
	public ResponseEntity<ApiResponse<Void>> handlerPriorityTransitionException(PriorityTransitionException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(false, ex.getMessage(), null));
	}

	@ExceptionHandler(TaskNotFoundException.class)
	public ResponseEntity<ApiResponse<Void>> handlerTaskNotFoundException(TaskNotFoundException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(false, ex.getMessage(), null));
	}

	@ExceptionHandler(UserMismatchException.class)
	public ResponseEntity<ApiResponse<Void>> handlerUserMismatchException(UserMismatchException ex) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse<>(false, ex.getMessage(), null));
	}

	@ExceptionHandler(DuplicateRequestException.class)
	public ResponseEntity<ApiResponse<Void>> handlerDuplicate(DuplicateRequestException ex) {
		ApiResponse<Void> response = new ApiResponse<>(
			false,
			ex.getMessage(),
			null
		);
		return ResponseEntity.badRequest().body(response);
	}

	@ExceptionHandler(UsernameNotFoundException.class)
	public ResponseEntity<ApiResponse<Void>> handlerUserIdNotFoundException(UsernameNotFoundException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(false, ex.getMessage(), null));
	}

	@ExceptionHandler(LoginFailedException.class)
	public ResponseEntity<ApiResponse<Void>> handlerLoginFailedException(LoginFailedException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(false, ex.getMessage(), null));
	}

	@ExceptionHandler(LogNotFoundException.class)
	public ResponseEntity<ApiResponse<Void>> handlerLogNotFoundException(LogNotFoundException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, ex.getMessage(), null));
	}

	@ExceptionHandler(BlacklistedTokenException.class)
	public ResponseEntity<ApiResponse<Void>> handlerBlacklistedTokenException(BlacklistedTokenException ex) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
			.body(new ApiResponse<>(false, ex.getMessage(), null));
	}

}