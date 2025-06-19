package com.example.taskflow.task.entity;

import com.example.taskflow.task.exception.StatusTransitionException;
import com.fasterxml.jackson.annotation.JsonCreator;

public enum Status {

	TODO, IN_PROGRESS, DONE;

	@JsonCreator
	public static Status from(String value) {
		try {
			return Status.valueOf(value.toUpperCase());
		} catch (IllegalArgumentException e) {
			throw new StatusTransitionException("지원하지 않는 상태값입니다.");
		}
	}

}