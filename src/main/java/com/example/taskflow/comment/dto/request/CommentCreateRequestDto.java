package com.example.taskflow.comment.dto.request;

import lombok.Getter;

@Getter
public class CommentCreateRequestDto {
    private final Long taskId;
    private final String content;

    public CommentCreateRequestDto(Long taskId, String content) {
        this.taskId = taskId;
        this.content = content;
    }
}

