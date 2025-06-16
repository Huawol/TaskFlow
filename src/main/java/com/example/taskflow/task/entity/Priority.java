package com.example.taskflow.task.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Priority {
	LOW, MEDIUM, HIGH;

	@JsonCreator
	public static Priority from(String value) {
		return Priority.valueOf(value.toUpperCase());
	}
}
