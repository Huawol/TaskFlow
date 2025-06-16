package com.example.taskflow.log.dto.response;

import com.example.taskflow.log.ActivityLog;
import com.example.taskflow.log.ActivityType;
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

    private Long userId;

    private ActivityType activityType;

    private Long targetId;

    private LocalDateTime timestamp;

    private String description;


    public static ActivityLogResponseDto toDto(ActivityLog log) {
        return ActivityLogResponseDto.builder()
                .activityType(log.getActivityType())
                .userId(log.getUserId())
                .timestamp(log.getTimestamp())
                .targetId(log.getTargetId())
                .description(log.getDescription())
                .build();
    }
}
