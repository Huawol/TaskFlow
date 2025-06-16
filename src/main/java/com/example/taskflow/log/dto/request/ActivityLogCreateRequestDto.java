package com.example.taskflow.log.dto.request;


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
public class ActivityLogCreateRequestDto {

    private Long userId;

    private String ipAddress;

    private String url;

    private String httpMethod;

    private ActivityType activityType;

    private Long targetId;

    private LocalDateTime timestamp;

    private String beforeStatus;

    private String afterStatus;

}
