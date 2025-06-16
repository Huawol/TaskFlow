package com.example.taskflow.common.exception;

import java.util.stream.Collectors;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.taskflow.common.ApiResponse;
import com.example.taskflow.task.exception.InvalidTaskPriorityTransitionException;
import com.example.taskflow.task.exception.PriorityTransitionException;
import com.example.taskflow.task.exception.StatusTransitionException;

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

	@ExceptionHandler(TodoNotFoundException.class)
	public ResponseEntity<ApiResponse<Void>> handlerTodoNotFoundException(TodoNotFoundException ex) {
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
}
