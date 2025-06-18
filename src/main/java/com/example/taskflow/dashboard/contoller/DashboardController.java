package com.example.taskflow.dashboard.contoller;

import com.example.taskflow.dashboard.dto.DashboardStatisticsDto;
import com.example.taskflow.dashboard.service.DashboardService;
import com.example.taskflow.common.ApiResponse;
import com.example.taskflow.security.dto.AuthUserDto;
import com.example.taskflow.task.dto.response.TaskResponseDto;
import com.example.taskflow.task.entity.Status;
import com.example.taskflow.task.service.TaskService;
import com.example.taskflow.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    // 이제 안씀....
//    @GetMapping("/statistics")
//    public ApiResponse<DashboardStatisticsDto> statistics(
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
//        if (from.isAfter(to)) {
//            throw new IllegalArgumentException("'from' must be before 'to'");
//        }
//        return ApiResponse.createSuccess("통계 조회 성공", dashboardService.getStatistics(from, to));
//    }


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

    @GetMapping("/tasks/status") // 상태의 태스크 목록확인
    public ApiResponse<?> getTaskByStatus(@RequestParam Status status) {
        return ApiResponse.createSuccess("조회 성공", dashboardService.getTaskByStatus(status));
    }
}
