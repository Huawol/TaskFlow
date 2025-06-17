package com.example.taskflow.comment.controller;

import com.example.taskflow.comment.dto.request.CommentCreateRequestDto;
import com.example.taskflow.comment.dto.response.FindAllCommentResponseDto;
import com.example.taskflow.comment.dto.request.UpdateCommentRequestDto;
import com.example.taskflow.comment.service.CommentService;
import com.example.taskflow.user.entity.User;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/comments")
@AllArgsConstructor
public class CommentController {
    private final CommentService commentService;

    private User getLoginUser(HttpSession session) {
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }
        return loginUser;
    }


    @PostMapping
    public ResponseEntity<Long> saveComment(
            @RequestBody CommentCreateRequestDto requestDto
    ) {

        Long commentId = commentService.saveComment(
                requestDto.getTaskId(),
                requestDto.getContent()
        );

        return new ResponseEntity<>(commentId, HttpStatus.CREATED);
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<Long> updateCommentById(
            @PathVariable Long commentId,
            @RequestBody UpdateCommentRequestDto requestDto,
            HttpSession session
    ) {
        User loginUser = getLoginUser(session);

        commentService.updateCommentById(loginUser.getId(), commentId, requestDto);
        return new ResponseEntity<>(commentId, HttpStatus.OK);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteCommentById(
            @PathVariable Long commentId,
            HttpSession session
    ) {
        User loginUser = getLoginUser(session);
        commentService.deleteCommentById(loginUser.getId(), commentId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<FindAllCommentResponseDto>> findAllComment() {
        List<FindAllCommentResponseDto> findAllCommentResponseDtoList = commentService.findAllComment();
        return new ResponseEntity<>(findAllCommentResponseDtoList, HttpStatus.OK);
    }
}
