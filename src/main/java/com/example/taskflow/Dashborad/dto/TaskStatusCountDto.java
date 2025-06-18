package com.example.taskflow.Dashborad.dto;

import com.example.taskflow.task.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class TaskStatusCountDto {

    private Status status; // task enum쓰는거
    private long count;
}
