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

	//null 값 허용할지 고민중
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate deadline;

	//초기값이 Todo 이므로 생성 시 미사용 필드
	// private String status;

	//초기값이 세팅 되어 있음 -> 추후 프론트와 연동시 초기세팅이 있을거임
	private String priority;

}
