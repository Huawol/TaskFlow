package com.example.taskflow.log.dto.response;

import com.example.taskflow.log.ActivityType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ActivityCreateResponseDto {

    private LocalDateTime timestamp;

    private Long userId;

    private ActivityType activityType;

    private Long targetId;

    private String description;

}
