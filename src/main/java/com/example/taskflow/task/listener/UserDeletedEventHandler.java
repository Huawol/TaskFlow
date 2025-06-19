package com.example.taskflow.task.listener;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.example.taskflow.task.service.TaskService;
import com.example.taskflow.user.event.UserDeletedEvent;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserDeletedEventHandler {

	private final TaskService taskService;

	/*
	유저 삭제 이벤트 리스너
	- 이벤트에서 받아온 삭제된 유저의 아이디값으로 테스크 서비스의 후처리를 진행
	 */
	@EventListener
	public void handleUserDeleted(UserDeletedEvent event) {
		taskService.ownerDeletionForTask(event.getUserId());
	}

}