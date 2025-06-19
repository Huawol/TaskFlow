package com.example.taskflow.dashboard.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.taskflow.dashboard.dto.ProgressRatioDto;
import com.example.taskflow.dashboard.dto.TaskStatusCountDto;
import com.example.taskflow.dashboard.dto.TaskStatusRatioDto;
import com.example.taskflow.dashboard.dto.TasksCountResponseDto;
import com.example.taskflow.dashboard.repository.TaskStatisticsRepository;
import com.example.taskflow.task.dto.response.TaskResponseDto;
import com.example.taskflow.task.entity.Status;
import com.example.taskflow.task.entity.Task;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class DashboardService {

	private final TaskStatisticsRepository taskStatisticsRepository;

	private double rate(long part, long all) {
		if (all == 0)
			return 0;
		return Math.round(part * 10000.0 / all) / 100.0; // 소수점 둘째 자리
	}

	public TaskStatusRatioDto getStatusRatio() {
		List<TaskStatusCountDto> result = taskStatisticsRepository.countGroupByStatus();

		long todo = 0, inProgress = 0, done = 0;

		for (TaskStatusCountDto dto : result) {
			switch (dto.getStatus()) {
				case TODO -> todo = dto.getCount();
				case IN_PROGRESS -> inProgress = dto.getCount();
				case DONE -> done = dto.getCount();
			}
		}

		return TaskStatusRatioDto.of(todo, inProgress, done);
	}

	public ProgressRatioDto getProgressRatio(Long userId) {
		Map<Status, Long> myMap = toMap(taskStatisticsRepository.countMyStatus(userId));

		long myDone = myMap.getOrDefault(Status.DONE, 0L);
		long myTotal = myMap.values().stream().mapToLong(Long::longValue).sum();

		double myRate = rate(myDone, myTotal);

		return ProgressRatioDto.of(myRate);
	}

	public List<TaskResponseDto> getTaskByStatus(Status status) {
		List<Task> foundTasks = taskStatisticsRepository.searchTaskByStatus_Todo(status);

		return foundTasks.stream().map(TaskResponseDto::from).toList();
	}

	private Map<Status, Long> toMap(List<TaskStatusCountDto> list) {
		return list.stream()
			.collect(Collectors.toMap(
				TaskStatusCountDto::getStatus,
				TaskStatusCountDto::getCount));
	}

	@Transactional(readOnly = true)
	public TasksCountResponseDto findTasksCount() {
		Long count = taskStatisticsRepository.countAllTasks();
		return TasksCountResponseDto.of(count);
	}

	public List<TaskResponseDto> foundTasksSortedByPriority() {
		List<Task> foundTasks = taskStatisticsRepository.findTasksByQuery();
		return foundTasks.stream().map(TaskResponseDto::from).toList();
	}

}