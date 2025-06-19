package com.example.taskflow.comment.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.taskflow.comment.dto.request.UpdateCommentRequestDto;
import com.example.taskflow.comment.dto.response.FindAllCommentResponseDto;
import com.example.taskflow.comment.entity.Comment;
import com.example.taskflow.comment.repository.CommentRepository;
import com.example.taskflow.log.aop.ActivityLogging;
import com.example.taskflow.log.entity.ActivityType;
import com.example.taskflow.task.entity.Task;
import com.example.taskflow.task.repository.TaskRepository;
import com.example.taskflow.user.entity.User;
import com.example.taskflow.user.repository.UserRepository;

@Service
@Transactional
public class CommentService {

	private final CommentRepository commentRepository;
	private final UserRepository userRepository;
	private final TaskRepository taskRepository;

	public CommentService(CommentRepository commentRepository, UserRepository userRepository,
		TaskRepository taskRepository) {
		this.commentRepository = commentRepository;
		this.userRepository = userRepository;
		this.taskRepository = taskRepository;
	}

	@ActivityLogging(value = ActivityType.COMMENT_CREATED, targetParam = "userId")
	public Long saveComment(Long userId, Long taskId, String content) {
		Task task = taskRepository.findById(taskId).orElseThrow(
			() -> new IllegalArgumentException("해당 Task가 존재하지 않습니다. id: " + taskId));

		User user = userRepository.findById(userId).orElseThrow(
			() -> new IllegalArgumentException("해당 User가 존재하지 않습니다. id: " + userId));

		Comment comment = new Comment(task, user, content);
		commentRepository.save(comment);

		return comment.getId();
	}

	@ActivityLogging(value = ActivityType.COMMENT_UPDATED, targetParam = "userId")
	public void updateCommentById(Long userId, Long commentId, UpdateCommentRequestDto requestDto) {
		Comment findComment = commentRepository.findByIdOrElseThrow(commentId);
		if (!findComment.getUser().getId().equals(userId)) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "댓글 수정 권한이 없습니다.");
		}
		findComment.updateComment(requestDto.getContent());
	}

	@ActivityLogging(value = ActivityType.COMMENT_DELETED, targetParam = "userId")
	public void deleteCommentById(Long userId, Long commentId) { // hard delete
		Comment findComment = commentRepository.findByIdOrElseThrow(commentId);

		Long commentOwnerId = findComment.getUser().getId();
		Long postOwnerId = findComment.getTask().getCreatedBy().getId();

		if (!userId.equals(commentOwnerId) && !userId.equals(postOwnerId)) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "댓글 삭제 권한이 없습니다.");
		}

		findComment.softDelete();
	}

	@Transactional(readOnly = true)
	public List<FindAllCommentResponseDto> findAllComment() {
		List<Comment> comments = commentRepository.findByDeletedFalse();
		return comments.stream().map(FindAllCommentResponseDto::toDto).toList();
	}

	/*
	테스크삭제에 따른 추가 로직
	해당 테스크에 맵핑된 코멘트 리스트를 논리적삭제시킴
	*/
	public void deleteCommentsByTaskDeletion(Long taskId) {
		List<Comment> foundComments = commentRepository.findByTaskIdAndDeletedFalse(taskId);
		foundComments.forEach(Comment::softDelete);
		commentRepository.saveAll(foundComments);
	}

}