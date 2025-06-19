package com.example.taskflow.comment.listener;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.example.taskflow.comment.service.CommentService;
import com.example.taskflow.task.event.TaskDeletedEvent;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TaskDeletedEventHandler {

	private final CommentService commentService;

	/*
	할일 삭제 이벤트 리스너
	- 이벤트에서 받아온 삭제된 할일의 아이디값으로
	 */
	@EventListener
	public void handleTaskDeleted(TaskDeletedEvent event) {
		commentService.deleteCommentsByTaskDeletion(event.getTaskId());
	}

}