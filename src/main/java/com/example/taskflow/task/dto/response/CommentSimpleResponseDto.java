package com.example.taskflow.task.dto.response;

import java.time.LocalDateTime;

import com.example.taskflow.comment.entity.Comment;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentSimpleResponseDto {

	private Long id;

	private String content;

	private LocalDateTime createdAt;

	public static CommentSimpleResponseDto from(Comment comment) {
		return new CommentSimpleResponseDto(comment.getId(), comment.getContent(), comment.getCreatedAt());
	}

}