package com.example.taskflow.log.dto.request;


import com.example.taskflow.log.entity.ActivityType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ActivityLogCreateRequestDto {

    @NotNull
    private ActivityType activityType;

    @NotNull
    private Long targetId;


    //TASK_STATUS_CHANGED 에서만 사용
    private String beforeStatus;

    private String afterStatus;

}
