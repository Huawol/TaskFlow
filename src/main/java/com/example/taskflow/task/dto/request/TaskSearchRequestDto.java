package com.example.taskflow.task.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TaskSearchRequestDto {
	private String title;
	private String content;
	private String status;
}
