package com.example.taskflow.task.controller;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.taskflow.common.ApiResponse;
import com.example.taskflow.security.dto.AuthUserDto;
import com.example.taskflow.task.dto.request.TaskCreateRequestDto;
import com.example.taskflow.task.dto.request.TaskStatusRequestDto;
import com.example.taskflow.task.dto.request.TaskUpdateRequestDto;
import com.example.taskflow.task.dto.response.TaskResponseDto;
import com.example.taskflow.task.dto.response.TaskWithCommentResponseDto;
import com.example.taskflow.task.service.TaskService;
import com.example.taskflow.user.entity.User;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tasks")
public class TaskController {

	private final TaskService taskService;

	@PreAuthorize("isAuthenticated()")
	@PostMapping
	public ResponseEntity<ApiResponse<TaskResponseDto>> createTask(
		@Valid @RequestBody TaskCreateRequestDto requestDto, @AuthenticationPrincipal AuthUserDto authUserDto) {
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(new ApiResponse<>(true, "정상적으로 할일이 생성되었습니다.", taskService.saveTask(authUserDto.getId(), requestDto)));
	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<TaskWithCommentResponseDto>> getTaskWithCommentById(@PathVariable Long id) {
		return ResponseEntity.status(HttpStatus.OK)
			.body(new ApiResponse<>(true, "정상적으로 할일을 조회했습니다.", taskService.findTaskWithCommentById(id)));
	}

	//단건 조회 코멘트도 같이 불러옴
	@PreAuthorize("isAuthenticated()")
	@PatchMapping("/{id}")
	public ResponseEntity<ApiResponse<TaskWithCommentResponseDto>> updateStatusTask(
		@PathVariable Long id, @Valid @RequestBody TaskStatusRequestDto requestDto, @AuthenticationPrincipal AuthUserDto authUserDto) {
		return ResponseEntity.status(HttpStatus.OK)
			.body(new ApiResponse<>(true, "정상적으로 상태를 변경했습니다.", taskService.changeStatusTask(id, requestDto, authUserDto)));
	}

	//날짜 검색 기능
	@PreAuthorize("isAuthenticated()")
	@GetMapping
	public ResponseEntity<ApiResponse<Page<TaskResponseDto>>> getPagedTasks(
		@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
		@RequestParam @DateTimeFormat(pattern = "yyyyMMdd")LocalDate periodStart, @RequestParam @DateTimeFormat(pattern = "yyyyMMdd")LocalDate periodEnd
	) {
		Pageable pageable = PageRequest.of(page, size, Sort.by("updatedAt").descending());
		return ResponseEntity.status(HttpStatus.OK)
			.body(new ApiResponse<>(true, "정상적으로 할일을 조회했습니다.", taskService.findPagedTasks(pageable, periodStart, periodEnd)));
	}

	@PreAuthorize("isAuthenticated()")
	@PutMapping("/{id}")
	public ResponseEntity<ApiResponse<TaskResponseDto>> updateTask(
		@PathVariable Long id, @Valid @RequestBody TaskUpdateRequestDto requestDto, @AuthenticationPrincipal AuthUserDto authUserDto) {
		return ResponseEntity.status(HttpStatus.OK)
			.body(new ApiResponse<>(true, "정상적으로 수정 되었습니다.", taskService.changeTask(id, requestDto, authUserDto)));
	}

	@PreAuthorize("isAuthenticated()")
	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse<Void>> deleteTask(
		@PathVariable Long id, @AuthenticationPrincipal AuthUserDto authUserDto) {
		taskService.softDeleteTask(id, authUserDto);
		return ResponseEntity.status(HttpStatus.OK)
			.body(new ApiResponse<>(true, "정상적으로 삭제 되었습니다.", null));
	}


}
