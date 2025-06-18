package com.example.taskflow.task.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.example.taskflow.comment.entity.Comment;
import com.example.taskflow.task.entity.Priority;
import com.example.taskflow.task.entity.Status;
import com.example.taskflow.task.entity.Task;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TaskWithCommentResponseDto {

	private Long id;

	private Long assignedToId;

	private String title;

	private String content;

	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate startDate;

	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate deadline;

	private List<CommentSimpleResponseDto> comments;

	private Status status;

	private Priority priority;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime createdAt;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime updatedAt;

	public static TaskWithCommentResponseDto from(Task task, List<CommentSimpleResponseDto> comments) {
		return new TaskWithCommentResponseDto(
			task.getId(),
			task.getAssignedTo().getId(),
			task.getTitle(),
			task.getContent(),
			task.getStartDate(),
			task.getDeadline(),
			comments,
			task.getStatus(),
			task.getPriority(),
			task.getCreatedAt(),
			task.getUpdatedAt()
		);
	}
}
