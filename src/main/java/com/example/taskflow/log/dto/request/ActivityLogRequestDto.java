package com.example.taskflow.log.dto.request;

import com.example.taskflow.log.ActivityType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ActivityLogRequestDto {

    private Long userId;

    private String ipAddress;

    private String url;

    private String httpMethod;

    private ActivityType activityType;

    private Long targetId;

    private LocalDateTime timestamp;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate startDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate endDate;
}
