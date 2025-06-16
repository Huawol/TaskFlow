package com.example.taskflow.task.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.taskflow.common.ApiResponse;
import com.example.taskflow.task.dto.request.TaskCreateRequestDto;
import com.example.taskflow.task.dto.request.TaskStatusRequestDto;
import com.example.taskflow.task.dto.response.TaskResponseDto;
import com.example.taskflow.task.dto.response.TaskWithCommentResponseDto;
import com.example.taskflow.task.service.TaskService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tasks")
public class TaskController {

	private final TaskService taskService;

	@PreAuthorize("isAuthenticated()")
	@PostMapping
	public ResponseEntity<ApiResponse<TaskResponseDto>> createTask(@Valid @RequestBody TaskCreateRequestDto requestDto) {
		Long id = 1L;

		return ResponseEntity.status(HttpStatus.CREATED)
			.body(new ApiResponse<>(true, "정상적으로 할일이 생성되었습니다.", taskService.saveTask(id, requestDto)));
	}

	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<TaskWithCommentResponseDto>> getTaskWithCommentById(@PathVariable Long id) {
		return ResponseEntity.status(HttpStatus.OK)
			.body(new ApiResponse<>(true, "정상적으로 할일을 조회했습니다.", taskService.findTaskWithCommentById(id)));
	}

	@PatchMapping("/{id}")
	public ResponseEntity<ApiResponse<TaskWithCommentResponseDto>> statusUpdateComment(
		@PathVariable Long id, @Valid @RequestBody TaskStatusRequestDto requestDto) {
		return ResponseEntity.status(HttpStatus.OK)
			.body(new ApiResponse<>(true, "정상적으로 상태를 변경했습니다.", taskService.changeStatusComment(id, requestDto)));
	}

}
