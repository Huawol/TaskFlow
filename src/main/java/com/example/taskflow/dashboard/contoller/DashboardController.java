package com.example.taskflow.dashboard.contoller;

import com.example.taskflow.dashboard.service.DashboardService;
import com.example.taskflow.common.ApiResponse;
import com.example.taskflow.security.dto.AuthUserDto;
import com.example.taskflow.task.entity.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dashboards")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/status-count") // 상태별 태스크 수 확인 ( 한번에 3개 조회함 )
    public ApiResponse<?> getDashboard() {
        return ApiResponse.createSuccess("조회 성공", dashboardService.getStatusRatio());
    }

    @GetMapping("/status-summary") // 완료율
    public ApiResponse<?> getDashboardRatio(
            @AuthenticationPrincipal AuthUserDto userDto
    ) {
        return ApiResponse.createSuccess("조회 성공", dashboardService.getProgressRatio(userDto.getId()));
    }

    @GetMapping("/tasks") // 상태의 태스크 목록확인
    public ApiResponse<?> getTaskByStatus(@RequestParam Status status) {
        return ApiResponse.createSuccess("조회 성공", dashboardService.getTaskByStatus(status));
    }
}
