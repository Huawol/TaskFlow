package com.example.taskflow.log.dto.response;

import com.example.taskflow.log.entity.ActivityLog;
import com.example.taskflow.log.entity.ActivityType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActivityLogResponseDto {

    private String username;

    private ActivityType activityType;

    private Long targetId;

    private LocalDateTime timestamp;

    private String description;

    public ActivityLogResponseDto(ActivityLog log){
        this.activityType = log.getActivityType();
        this.targetId = log.getTargetId();
        this.description = log.getDescription();
        this.timestamp = log.getTimestamp();
    }

    public static ActivityLogResponseDto toDto(ActivityLog log, String username) {
        return  ActivityLogResponseDto.builder()
                .timestamp(log.getTimestamp())
                .username(username)
                .activityType(log.getActivityType())
                .targetId(log.getTargetId())
                .description(log.getDescription())
                .build();
    }
}
