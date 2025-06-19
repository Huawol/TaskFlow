package com.example.taskflow.log.dto.response;

import java.time.LocalDateTime;

import com.example.taskflow.log.entity.ActivityType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

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