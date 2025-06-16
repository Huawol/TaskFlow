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
import com.example.taskflow.task.dto.response.CommentSimpleResponseDto;
import com.example.taskflow.task.dto.response.TaskResponseDto;
import com.example.taskflow.task.dto.response.TaskWithCommentResponseDto;
import com.example.taskflow.task.entity.Task;
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

	public TaskWithCommentResponseDto changeStatusComment(Long id) {
	}


	// 헬퍼 메서드
	private User getUserOrThrow(Optional<User> user) {
		return user.orElseThrow(() -> new UserNotFoundException("ID 값과 일치하는 유저를 찾을 수 없습니다."));
	}

	private Task getTaskOrThrow(Optional<Task> todo) {
		return todo.orElseThrow(() -> new TodoNotFoundException("ID 값과 일치하는 유저를 찾을 수 없습니다."));
	}

}
