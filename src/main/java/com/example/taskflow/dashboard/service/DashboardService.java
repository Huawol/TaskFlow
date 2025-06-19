package com.example.taskflow.dashboard.service;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import com.example.taskflow.task.repository.TaskRepository;

@Service
@RequiredArgsConstructor
public class DashboardService {
    private final TaskRepository taskRepository;

    //삭제되지 않은 Task의 총 개수를 반환
    public long getTotalActiveTasks() {
        return taskRepository.countByDeletedFalse();
    }
}
