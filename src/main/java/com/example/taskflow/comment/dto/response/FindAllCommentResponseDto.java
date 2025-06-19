package com.example.taskflow.comment.dto.response;

import java.time.LocalDateTime;

import com.example.taskflow.comment.entity.Comment;

import lombok.Getter;

@Getter
public class FindAllCommentResponseDto {

	private final Long commentId;
	private final String content;
	private final String author;
	private final LocalDateTime createAt;
	private final LocalDateTime updateAt;

	public FindAllCommentResponseDto(Long commentId, String content, String author, LocalDateTime createAt,
		LocalDateTime updateAt) {
		this.commentId = commentId;
		this.content = content;
		this.author = author;
		this.createAt = createAt;
		this.updateAt = updateAt;
	}

	public static FindAllCommentResponseDto toDto(Comment comment) {
		return new FindAllCommentResponseDto(
			comment.getId(),
			comment.getContent(),
			comment.getUser().getUsername(),
			comment.getCreatedAt(),
			comment.getUpdatedAt()
		);
	}

}
