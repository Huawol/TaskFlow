package com.example.taskflow.dashboard.controller;

import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import com.example.taskflow.dashboard.service.DashboardService;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dashboards")
public class DashboardController {
    private final DashboardService dashboardService;

    //전체 Task수를 조회하는 API
    @GetMapping("/total-tasks")
    public Map<String, Long> getTotalTasks() {
        long totalTasks = dashboardService.getTotalActiveTasks();
        return Map.of("totalTasks", totalTasks);
    }
}
