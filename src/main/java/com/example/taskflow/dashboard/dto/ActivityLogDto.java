package com.example.taskflow.dashboard.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class ActivityLogDto {

	private String userName;
	private String message;
	private LocalDateTime activityDate;

}