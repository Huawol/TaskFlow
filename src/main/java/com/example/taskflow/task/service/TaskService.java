package com.example.taskflow.task.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.taskflow.comment.entity.Comment;
import com.example.taskflow.comment.repository.CommentRepository;
import com.example.taskflow.common.exception.TodoNotFoundException;
import com.example.taskflow.common.exception.UserNotFoundException;
import com.example.taskflow.task.dto.request.TaskCreateRequestDto;
import com.example.taskflow.task.dto.request.TaskStatusRequestDto;
import com.example.taskflow.task.dto.response.CommentSimpleResponseDto;
import com.example.taskflow.task.dto.response.TaskResponseDto;
import com.example.taskflow.task.dto.response.TaskWithCommentResponseDto;
import com.example.taskflow.task.entity.Status;
import com.example.taskflow.task.entity.Task;
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

	public TaskResponseDto saveTask(Long createdById, TaskCreateRequestDto requestDto) {
		//테스트용 로직
		User user1 = new User();
		User user2 = new User();
		userRepository.save(user1);
		userRepository.save(user2);

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
	public TaskWithCommentResponseDto changeStatusComment(Long id, TaskStatusRequestDto requestDto) {
		Task foundTask = getTaskOrThrow(taskRepository.findByIdAndDeletedFalse(id));

		//변경을 원하는 상태값 enum 값으로 변경 후 검증 메서드 호출
		Status requestStatus = Status.from(requestDto.getStatus());

		if(!inValidTransition(foundTask.getStatus(), requestStatus)) {
			throw new StatusTransitionException("상태값은 TODO -> IN_PROGRESS -> DONE 순으로만 변경 가능합니다.");
		}

		if(requestStatus.equals(Status.IN_PROGRESS)){
			//변경값이 TODO -> IN_PROGRESS 일 경우 시작일 세팅
			foundTask.setStartDate();
		}

		foundTask.changeStatus(requestDto.getStatus()); //더티체킹
		List<Comment> comments = commentRepository.findByTaskIdAndDeletedFalse(id);
		List<CommentSimpleResponseDto> commentSimpleResponses = comments.stream().map(CommentSimpleResponseDto::from).collect(Collectors.toList());

		return TaskWithCommentResponseDto.from(foundTask, commentSimpleResponses);
	}


	// 헬퍼 메서드
	private User getUserOrThrow(Optional<User> user) {
		return user.orElseThrow(() -> new UserNotFoundException("ID 값과 일치하는 유저를 찾을 수 없습니다."));
	}

	private Task getTaskOrThrow(Optional<Task> todo) {
		return todo.orElseThrow(() -> new TodoNotFoundException("ID 값과 일치하는 유저를 찾을 수 없습니다."));
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

}
