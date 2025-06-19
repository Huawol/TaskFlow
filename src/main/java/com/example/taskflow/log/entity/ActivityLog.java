package com.example.taskflow.log.entity;

import java.time.LocalDateTime;

import com.example.taskflow.common.BaseEntity;
import com.example.taskflow.log.dto.request.ActivityLogCreateRequestDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "activity_logs")
@Builder
public class ActivityLog extends BaseEntity {

	@Id
	@GeneratedValue
	private Long id;

	@Column(name = "user_id", nullable = false)
	private Long userId; //사용자 ID;

	@Column(name = "ip_address", nullable = false, length = 255)
	private String ipAddress;

	@Column(name = "http_method", nullable = false, length = 10)
	private String httpMethod;

	@Column(name = "url", nullable = false, length = 255)
	private String url;

	@Column(name = "timestamp", nullable = false)
	private LocalDateTime timestamp;

	@Column(name = "target_id", nullable = false)
	private Long targetId;  //task,comment.user

	@Enumerated(EnumType.STRING)
	@Column(name = "activity_type", nullable = false)
	private ActivityType activityType;

	@Column(length = 255, nullable = false)
	private String description;

	public static ActivityLog create(ActivityLogCreateRequestDto requestDto, String description,
		Long userId) {
		return ActivityLog.builder()
			.userId(userId)
			.targetId(requestDto.getTargetId())
			.activityType(requestDto.getActivityType())
			.timestamp(LocalDateTime.now())
			.description(description)
			.build();
	}

	public void updateDescription(String description) {
		this.description = description;
	}

}