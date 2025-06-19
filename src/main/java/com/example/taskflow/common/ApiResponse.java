package com.example.taskflow.common;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.annotation.Nullable;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

	private Boolean success;
	private String message;
	private T data;
	private LocalDateTime timestamp;

	public static <T> ApiResponse<T> createSuccess(String message, @Nullable T data) {
		return new ApiResponse<>(true, message, data);
	}

	public ApiResponse(Boolean success, String message, T responseDto) {
		this.success = success;
		this.message = message;
		this.data = responseDto;
		this.timestamp = LocalDateTime.now();
	}

}