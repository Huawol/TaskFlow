package com.example.taskflow.log.entity;

import com.example.taskflow.log.dto.request.ActivityLogCreateRequestDto;

public enum ActivityType {
    TASK_CREATED("새로운 작업이 생성되었습니다."),
    TASK_UPDATED("작업이 수정 되었습니다."),
    TASK_DELETED("작업이 삭제 되었습니다."),
    TASK_STATUS_CHANGED(null), //상태 변경은 before,after 값으로 생성됨.
    COMMENT_CREATED(null), //userId  기반 메세지
    COMMENT_UPDATED(null),
    COMMENT_DELETED(null),
    USER_LOGGED_IN(null),
    USER_LOGGED_OUT(null);

    private final String message;

    ActivityType(String message){
        this.message = message;
    }

    //활동 유형에 따라 설명 메세지 동적 설정
    public String description(ActivityLogCreateRequestDto requestDto,String username) {
        return switch (this) {
            case TASK_STATUS_CHANGED -> String.format("작업 상태가 %s에서 %s로 변경되었습니다.",
                    requestDto.getBeforeStatus(), requestDto.getAfterStatus());
            case USER_LOGGED_IN -> String.format("%s가 로그인했습니다.", username);
            case USER_LOGGED_OUT -> String.format("%s가 로그아웃했습니다.", username);
            case COMMENT_CREATED -> String.format("%s의 댓글이 작업되었습니다.", username);
            case COMMENT_UPDATED -> String.format("%s의 댓글이 수정되었습니다.", username);
            case COMMENT_DELETED -> String.format("%s의 댓글이 삭제되었습니다.", username);
            default -> message; //static 메세지가 정의된 경우 사용
        };

    }
}
