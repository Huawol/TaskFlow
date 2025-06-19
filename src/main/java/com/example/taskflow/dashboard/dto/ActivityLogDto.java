package com.example.taskflow.dashboard.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor(staticName = "of")
public class ActivityLogDto {

    private String userName;
    private String message;
    private LocalDateTime activityDate;
}
