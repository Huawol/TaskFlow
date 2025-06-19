package com.example.taskflow.task.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class TaskStatusRequestDto {

	@NotBlank(message = "변경할 상태값을 입력하세요")
	private String status;

}