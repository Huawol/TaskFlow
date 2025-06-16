package com.example.taskflow.log.dto.response;

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

    private ActivityType activityType;

    private Long targetId;
}
