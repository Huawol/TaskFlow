package com.example.taskflow.task.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

//테스크 삭제 이벤트
@AllArgsConstructor
@Getter
public class TaskDeletedEvent {
	private Long taskId;
}
