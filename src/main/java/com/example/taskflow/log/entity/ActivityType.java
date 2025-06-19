package com.example.taskflow.log.entity;

import com.example.taskflow.log.dto.request.ActivityLogCreateRequestDto;

public enum ActivityType {
    TASK_CREATED("새로운 작업이 생성되었습니다."),
    TASK_UPDATED("작업이 수정 되었습니다."),
    TASK_DELETED("작업이 삭제 되었습니다."),
    TASK_STATUS_CHANGED(null), //상태 변경은 before,after 값으로 생성됨.
    COMMENT_CREATED("댓글이 생성되었습니다."), //userId  기반 메세지
    COMMENT_UPDATED("댓글이 수정되었습니다."),
    COMMENT_DELETED("댓글이 삭제되었습니다."),
    USER_LOGGED_IN("로그인했습니다."),
    USER_LOGGED_OUT("로그아웃했습니다.");

    private final String message;

    ActivityType(String message){
        this.message = message;
    }

    //활동 유형에 따라 설명 메세지 동적 설정
    public String description(ActivityLogCreateRequestDto requestDto) {
        if (this == TASK_STATUS_CHANGED) {
            //<조건> ? <참일때 값> : <거짓일떄 값>
            String before = requestDto != null ? requestDto.getBeforeStatus() : "알 수 없음";
            String after = requestDto != null ? requestDto.getAfterStatus() : "알 수 없음";
            return String.format("작업 상태가 %s에서 %s로 변경되었습니다.", before, after);
        }
        return message;
    }
}