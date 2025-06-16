package com.example.taskflow.log.dto.request;


import com.example.taskflow.log.ActivityType;
import jakarta.validation.constraints.NotNull;
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

    @NotNull
    private Long userId;

    @NotNull
    private ActivityType activityType;

    @NotNull
    private Long targetId;

    @NotNull
    private LocalDateTime timestamp;
}
