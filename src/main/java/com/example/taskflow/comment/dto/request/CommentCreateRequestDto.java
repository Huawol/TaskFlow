package com.example.taskflow.comment.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentCreateRequestDto {

	private Long taskId;
	private String content;

	public CommentCreateRequestDto(Long taskId, String content) {
		this.taskId = taskId;
		this.content = content;
	}

}