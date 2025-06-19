package com.example.taskflow.comment.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.taskflow.comment.dto.request.CommentCreateRequestDto;
import com.example.taskflow.comment.dto.request.UpdateCommentRequestDto;
import com.example.taskflow.comment.dto.response.FindAllCommentResponseDto;
import com.example.taskflow.comment.service.CommentService;
import com.example.taskflow.common.ApiResponse;
import com.example.taskflow.security.dto.AuthUserDto;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/tasks/comments")
@AllArgsConstructor
public class CommentController {

	private final CommentService commentService;

	@PostMapping
	public ResponseEntity<ApiResponse<Long>> saveComment(
		@RequestBody CommentCreateRequestDto requestDto,
		@AuthenticationPrincipal AuthUserDto authUserDto
	) {

		Long commentId = commentService.saveComment(
			authUserDto.getId(),
			requestDto.getTaskId(),
			requestDto.getContent()
		);

		return ResponseEntity.status(HttpStatus.CREATED)
			.body(new ApiResponse<>(true, "정상적으로 댓글이 생성되었습니다.", commentId));
	}

	@PatchMapping("/{commentId}")
	public ResponseEntity<ApiResponse<Long>> updateCommentById(
		@PathVariable Long commentId,
		@RequestBody UpdateCommentRequestDto requestDto,
		@AuthenticationPrincipal AuthUserDto authUserDto
	) {
		commentService.updateCommentById(authUserDto.getId(), commentId, requestDto);
		return ResponseEntity.status(HttpStatus.OK)
			.body(new ApiResponse<>(true, "정상적으로 댓글이 수정되었습니다.", commentId));
	}

	@DeleteMapping("/{commentId}")
	public ResponseEntity<ApiResponse<Void>> deleteCommentById(
		@PathVariable Long commentId,
		@AuthenticationPrincipal AuthUserDto authUserDto
	) {
		commentService.deleteCommentById(authUserDto.getId(), commentId);
		return ResponseEntity.status(HttpStatus.OK)
			.body(new ApiResponse<>(true, "정상적으로 댓글이 삭제되었습니다.", null));
	}

	@GetMapping
	public ResponseEntity<ApiResponse<List<FindAllCommentResponseDto>>> findAllComment() {
		List<FindAllCommentResponseDto> findAllCommentResponseDtoList = commentService.findAllComment();
		return ResponseEntity.status(HttpStatus.OK)
			.body(new ApiResponse<>(true, "정상적으로 댓글이 조회되었습니다.", findAllCommentResponseDtoList));
	}

}