package com.example.taskflow.log;

import com.example.taskflow.common.BaseEntity;
import com.example.taskflow.log.dto.request.ActivityLogCreateRequestDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "activity_logs")
@Builder
public class ActivityLog extends BaseEntity {
    @Id@GeneratedValue
    private Long id;

    private Long userId; //사용자 ID;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "http_method")
    private String httpMethod;

    @Column(name = "url")
    private String url;

    private LocalDateTime timestamp;

    //task,comment.user
    private Long target_id;

    @Enumerated(EnumType.STRING)
    private ActivityType activityType;

    //논리적  삭제
    private Boolean deleted = false;

    public static ActivityLog from(ActivityLogCreateRequestDto requestDto){
        return ActivityLog.builder()
                .userId(requestDto.getUserId())
                .target_id(requestDto.getTargetId())
                .activityType(requestDto.getActivityType())
                .timestamp(LocalDateTime.now())
                .build();
    }
}
