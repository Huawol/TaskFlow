package com.example.taskflow.comment.controller;

import com.example.taskflow.comment.dto.request.CommentCreateRequestDto;
import com.example.taskflow.comment.dto.response.FindAllCommentResponseDto;
import com.example.taskflow.comment.dto.request.UpdateCommentRequestDto;
import com.example.taskflow.comment.service.CommentService;
import com.example.taskflow.security.dto.AuthUserDto;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks/comments")
@AllArgsConstructor
public class CommentController {
    private final CommentService commentService;

    // private User getLoginUser(HttpSession session) {
    //     User loginUser = (User) session.getAttribute("loginUser");
    //     if (loginUser == null) {
    //         throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
    //     }
    //     return loginUser;
    // }


    @PostMapping
    public ResponseEntity<Long> saveComment(
            @RequestBody CommentCreateRequestDto requestDto,
            @AuthenticationPrincipal AuthUserDto authUserDto
    ) {

        Long commentId = commentService.saveComment(
                authUserDto.getId(),
                requestDto.getTaskId(),
                requestDto.getContent()
        );

        return new ResponseEntity<>(commentId, HttpStatus.CREATED);
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<Long> updateCommentById(
            @PathVariable Long commentId,
            @RequestBody UpdateCommentRequestDto requestDto,
            @AuthenticationPrincipal AuthUserDto authUserDto
    ) {
        commentService.updateCommentById(authUserDto.getId(), commentId, requestDto);
        return new ResponseEntity<>(commentId, HttpStatus.OK);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteCommentById(
            @PathVariable Long commentId,
        @AuthenticationPrincipal AuthUserDto authUserDto
    ) {
        commentService.deleteCommentById(authUserDto.getId(), commentId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<FindAllCommentResponseDto>> findAllComment() {
        List<FindAllCommentResponseDto> findAllCommentResponseDtoList = commentService.findAllComment();
        return new ResponseEntity<>(findAllCommentResponseDtoList, HttpStatus.OK);
    }
}
