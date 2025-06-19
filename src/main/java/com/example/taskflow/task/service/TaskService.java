package com.example.taskflow.task.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.context.ApplicationEventPublisher;
import com.example.taskflow.log.aop.ActivityLogging;
import com.example.taskflow.log.dto.request.ActivityLogCreateRequestDto;
import com.example.taskflow.log.entity.ActivityType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.taskflow.comment.entity.Comment;
import com.example.taskflow.comment.repository.CommentRepository;
import com.example.taskflow.common.exception.TaskNotFoundException;
import com.example.taskflow.common.exception.UserMismatchException;
import com.example.taskflow.common.exception.UserNotFoundException;
import com.example.taskflow.security.dto.AuthUserDto;
import com.example.taskflow.task.dto.request.TaskCreateRequestDto;
import com.example.taskflow.task.dto.request.TaskStatusRequestDto;
import com.example.taskflow.task.dto.request.TaskUpdateRequestDto;
import com.example.taskflow.task.dto.response.CommentSimpleResponseDto;
import com.example.taskflow.task.dto.response.TaskResponseDto;
import com.example.taskflow.task.dto.response.TaskWithCommentResponseDto;
import com.example.taskflow.task.entity.Status;
import com.example.taskflow.task.entity.Task;
import com.example.taskflow.task.event.TaskDeletedEvent;
import com.example.taskflow.task.exception.StatusTransitionException;
import com.example.taskflow.task.repository.TaskRepository;
import com.example.taskflow.user.entity.User;
import com.example.taskflow.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class TaskService {
	private final TaskRepository taskRepository;
	private final UserRepository userRepository;
	private final CommentRepository commentRepository;
	private final ApplicationEventPublisher eventPublisher;


	@ActivityLogging(value = ActivityType.TASK_CREATED, targetParam = "createdById")
	public TaskResponseDto saveTask(Long createdById, TaskCreateRequestDto requestDto) {
		//비즈니스 로직 시작
		User createdUser = getUserOrThrow(userRepository.findById(createdById));
		User assignedUser = getUserOrThrow(userRepository.findById(requestDto.getAssignedToId()));

		Task savedTask = taskRepository.save(Task.create(createdUser, assignedUser, requestDto.getTitle(),
			requestDto.getContent(), requestDto.getDeadline(), requestDto.getPriority()));

		return TaskResponseDto.from(savedTask);
	}

	@Transactional(readOnly = true)
	public TaskWithCommentResponseDto findTaskWithCommentById(Long id) {
		Task foundTask = getTaskOrThrow(taskRepository.findByIdAndDeletedFalse(id));
		List<Comment> comments = commentRepository.findByTaskIdAndDeletedFalse(id);
		List<CommentSimpleResponseDto> commentSimpleResponses = comments.stream().map(CommentSimpleResponseDto::from).collect(Collectors.toList());

		return TaskWithCommentResponseDto.from(foundTask, commentSimpleResponses);
	}

	//상태값 변경 메서드
	@ActivityLogging(value = ActivityType.TASK_STATUS_CHANGED, targetParam = "id")
	public TaskWithCommentResponseDto changeStatusTask(Long id, TaskStatusRequestDto requestDto, AuthUserDto authUserDto) {
		Task foundTask = getTaskOrThrow(taskRepository.findByIdAndDeletedFalse(id));

		//변경을 원하는 상태값 enum 값으로 변경 후 검증 메서드 호출
		Status requestStatus = Status.from(requestDto.getStatus());
		//이전상태 저장
		Status beforeStatus = foundTask.getStatus();

		if(!inValidTransition(foundTask.getStatus(), requestStatus)) {
			throw new StatusTransitionException("상태값은 TODO -> IN_PROGRESS -> DONE 순으로만 변경 가능합니다.");
		}

		if(requestStatus.equals(Status.IN_PROGRESS)){
			//변경값이 TODO -> IN_PROGRESS 일 경우 시작일 세팅
			foundTask.setStartDate();
		}

		foundTask.changeStatus(requestDto.getStatus()); //더티체킹
		List<Comment> comments = commentRepository.findByTaskIdAndDeletedFalse(id);
		List<CommentSimpleResponseDto> commentSimpleResponses = comments.stream()
			.map(CommentSimpleResponseDto::from)
			.collect(Collectors.toList());

		return TaskWithCommentResponseDto.from(foundTask, commentSimpleResponses);
	}

	@ActivityLogging(value = ActivityType.TASK_UPDATED, targetParam = "id")
	public TaskResponseDto changeTask(Long id, TaskUpdateRequestDto requestDto, AuthUserDto authUserDto) {
		Long authId = authUserDto.getId();
		Task foundTask = getTaskOrThrow(taskRepository.findByIdAndDeletedFalse(id));
		if(!isTaskOwner(foundTask.getCreatedBy().getId(), authId)) {
			throw new UserMismatchException("내가 작성하지 않은 게시물은 수정할 수 없습니다.");
		}

		//변경될 담당자 엔티티
		User assignedChangeUser = getUserOrThrow(userRepository.findById(authId));

		//변경사항 더티체킹
		foundTask.updateTaskFrom(assignedChangeUser, requestDto.getTitle(), requestDto.getContent(),
			requestDto.getDeadline(), requestDto.getPriority());

		return TaskResponseDto.from(foundTask);
	}

	@ActivityLogging(value = ActivityType.TASK_DELETED, targetParam = "createdById")
	public void softDeleteTask(Long id, AuthUserDto authUserDto) {
		Long authId = authUserDto.getId();
		Task foundTask = getTaskOrThrow(taskRepository.findByIdAndDeletedFalse(id));
		if (!isTaskOwner(foundTask.getCreatedBy().getId(), authId)) {
			throw new UserMismatchException("내가 작성하지 않은 게시물은 삭제할 수 없습니다.");
		}

		foundTask.softDelete(); //논리적 삭제 메서드 호출
		foundTask.setDeletedAt(); //삭제 시각 기록

		eventPublisher.publishEvent(new TaskDeletedEvent(foundTask.getId()));
	}

	/*
	날짜 기준으로 검색 메서드 LocalDate 로 받아 -> LocalDateTime 으로 변환시켜줘야함
	날짜 데이터가 없을시 전체 페이징 리스트 반환
	 */
	public Page<TaskResponseDto> findPagedTasks(Pageable pageable, LocalDate periodStart, LocalDate periodEnd) {
		//기간 설정 여부에 따라 다른 리포지터리 메서드 실행
		Page<Task> tasks;
		//시작기간, 종료기간 둘 중 하나라도 세팅이 안돼있을 시
		if(periodStart == null || periodEnd == null) {
			tasks = taskRepository.findAllByDeletedFalse(pageable);
		} else {
			//타입 일치를 위한 시간 세팅
			tasks = taskRepository.findByCreatedAtBetweenAndDeletedFalse(periodStart.atStartOfDay(), periodEnd.atTime(
				LocalTime.MAX), pageable);
		}

		//단건 조회에서만 코멘츠가 붙음 전체조회에선 변환해서 바로 반환
		return tasks.map(TaskResponseDto::from);
	}

	/*
	요구사항
	유저 탈퇴 시 해당 사용자가 담당한 태스크는 미할당 상태로 변경
	인자로 받아온 userId 와 assignedToId 일치하는 게시물 조회 후 assignedToId 필드를 null 로 처리할 것
	 */
	public void ownerDeletionForTask(Long userId) {
		List<Task> foundTasks = taskRepository.findByAssignedTo_IdAndDeletedFalse(userId);

		foundTasks.forEach(Task::unassignTask);
		taskRepository.saveAll(foundTasks);
	}

	// 헬퍼 메서드
	private User getUserOrThrow(Optional<User> user) {
		return user.orElseThrow(() -> new UserNotFoundException("ID 값과 일치하는 유저를 찾을 수 없습니다."));
	}

	private Task getTaskOrThrow(Optional<Task> todo) {
		return todo.orElseThrow(() -> new TaskNotFoundException("ID 값과 일치하는 할일을 찾을 수 없습니다."));
	}

	/*
	요구사항
	TODO -> IN_PROGRESS -> DONE 으로만 변경 가능 이전으로 상태 변경 불가
	요청에 담긴 status 값이 요구사항에 충족하는지 검증하는 메서드
	*/
	private boolean inValidTransition(Status currentStatus, Status requestStatus) {
		if(currentStatus.equals(Status.TODO) && requestStatus.equals(Status.IN_PROGRESS)) {
			return true;
		}
		if(currentStatus.equals(Status.IN_PROGRESS) && requestStatus.equals(Status.DONE)) {
			return true;
		}
		return false;
	}

	//인증객체의 id 값과 할일 생성자의 값 비교
	private boolean isTaskOwner(Long taskId, Long authId) {
		if (taskId.equals(authId)) {
			return true;
		}
		return false;
	}

}
