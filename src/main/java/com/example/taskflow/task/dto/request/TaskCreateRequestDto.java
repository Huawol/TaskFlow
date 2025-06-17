package com.example.taskflow.task.dto.request;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class TaskCreateRequestDto {

	@NotNull(message = "담당자를 입력해주세요")
	private Long assignedToId;

	@NotBlank(message = "타이틀을 입력해주세요.")
	private String title;

	@NotBlank(message = "내용을 입력해주세요.")
	private String content;

	@JsonFormat(pattern = "yyyyMMdd")
	@NotNull(message = "마감일을 설정해주세요.")
	private LocalDate deadline;

	@NotBlank(message = "중요도를 설정해주세요")
	private String priority;

}
