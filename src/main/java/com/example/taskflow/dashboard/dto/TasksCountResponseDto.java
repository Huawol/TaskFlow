package com.example.taskflow.dashboard.dto;


import com.example.taskflow.task.dto.response.TaskResponseDto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TasksCountResponseDto {
	private Long count;

	public static TasksCountResponseDto of(Long count) {
		return new TasksCountResponseDto(count);
	}
}
