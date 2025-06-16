package com.example.taskflow.log.entity;

import com.example.taskflow.common.BaseEntity;
import com.example.taskflow.log.dto.request.ActivityLogCreateRequestDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "activity_logs")
@Builder
public class ActivityLog extends BaseEntity {
    @Id@GeneratedValue
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

    @Column(length = 255,nullable = false)
    private String description;

    //논리적  삭제
    @Column(name = "deleted", nullable = false)
    private Boolean deleted = false;


    public void softDelete() {
        this.deleted = true;
    }

    public static ActivityLog create(ActivityLogCreateRequestDto requestDto, String newDescription){
        return ActivityLog.builder()
                .userId(requestDto.getUserId())
                .ipAddress(requestDto.getIpAddress())
                .httpMethod(requestDto.getHttpMethod())
                .url(requestDto.getUrl())
                .targetId(requestDto.getTargetId())
                .activityType(requestDto.getActivityType())
                .timestamp(LocalDateTime.now())
                .description(newDescription)
                .deleted(false)
                .build();
    }

    public void updateDescription(String description){
        this.description = description;
    }
}
