package com.example.taskflow.task.entity;

import com.example.taskflow.task.exception.PriorityTransitionException;
import com.fasterxml.jackson.annotation.JsonCreator;

public enum Priority {
	LOW, MEDIUM, HIGH;

	@JsonCreator
	public static Priority from(String value) {
		try{
			return Priority.valueOf(value.toUpperCase());
		} catch (IllegalArgumentException e) {
			throw new PriorityTransitionException("변경할 수 없는 요청값입니다.");
		}
	}
}
