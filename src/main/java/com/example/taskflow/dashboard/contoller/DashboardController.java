package com.example.taskflow.dashboard.contoller;

import java.util.List;

import com.example.taskflow.dashboard.dto.TasksCountResponseDto;
import com.example.taskflow.dashboard.service.DashboardService;
import com.example.taskflow.common.ApiResponse;
import com.example.taskflow.security.dto.AuthUserDto;
import com.example.taskflow.task.dto.response.TaskResponseDto;
import com.example.taskflow.task.entity.Status;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ApiResponse<?>> getDashboard() {
        return ResponseEntity.status(HttpStatus.OK)
            .body(ApiResponse.createSuccess("조회 성공", dashboardService.getStatusRatio()));
    }

    @GetMapping("/status-summary") // 완료율
    public ResponseEntity<ApiResponse<?>> getDashboardRatio(
            @AuthenticationPrincipal AuthUserDto userDto
    ) {
        return ResponseEntity.status(HttpStatus.OK)
            .body(ApiResponse.createSuccess("조회 성공", dashboardService.getProgressRatio(userDto.getId())));
    }

    @GetMapping("/tasks") // 상태의 태스크 목록확인
    public ResponseEntity<ApiResponse<?>> getTaskByStatus(@RequestParam Status status) {
        return ResponseEntity.status(HttpStatus.OK)
            .body(ApiResponse.createSuccess("조회 성공", dashboardService.getTaskByStatus(status)));
    }

    //전체 할일 카운팅 메서드
    @GetMapping("/tasks/total-count")
    public ResponseEntity<ApiResponse<TasksCountResponseDto>> getTasksCount() {
        return ResponseEntity.status(HttpStatus.OK)
            .body(ApiResponse.createSuccess("할일의 전체 갯수를 조회했습니다.", dashboardService.findTasksCount()));
    }

    @GetMapping("/priority")
    public ResponseEntity<ApiResponse<List<TaskResponseDto>>> getTasksByPriority() {
        return ResponseEntity.status(HttpStatus.OK)
            .body(ApiResponse.createSuccess("정상적으로 조회되었습니다.", dashboardService.foundTasksSortedByPriority()));
    }

}
