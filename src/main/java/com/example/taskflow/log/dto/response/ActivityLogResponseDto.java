package com.example.taskflow.log.dto.response;

import com.example.taskflow.log.entity.ActivityLog;
import com.example.taskflow.log.entity.ActivityType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ActivityLogResponseDto {

    private Long id;

    private Long userId;

    private ActivityType activityType;

    private Long targetId;

    private LocalDateTime timestamp;

    private String description;

    public ActivityLogResponseDto(ActivityLog log){
        this.id = log.getId();
        this.userId = log.getUserId();
        this.activityType = log.getActivityType();
        this.targetId = log.getTargetId();
        this.description = log.getDescription();
        this.timestamp = log.getTimestamp();
    }

    public static ActivityLogResponseDto toDto(ActivityLog log) {
        return new ActivityLogResponseDto(log);
    }
}
