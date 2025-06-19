package com.example.taskflow.user.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

// 유저 삭제시 발행될 이벤트
@AllArgsConstructor
@Getter
public class UserDeletedEvent {

	private Long userId;

}