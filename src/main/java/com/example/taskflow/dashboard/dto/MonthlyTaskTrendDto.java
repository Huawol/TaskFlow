package com.example.taskflow.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class MonthlyTaskTrendDto {

	private int month;
	private long totalCount;
	private long doneCount;

}