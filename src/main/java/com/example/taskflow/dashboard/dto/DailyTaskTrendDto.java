package com.example.taskflow.dashboard.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class DailyTaskTrendDto {

	private LocalDate date;
	private long totalCount;
	private long doneCount;

}