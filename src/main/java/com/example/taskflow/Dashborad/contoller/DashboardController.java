package com.example.taskflow.Dashborad.contoller;

import com.example.taskflow.Dashborad.dto.DashboardStatisticsDto;
import com.example.taskflow.Dashborad.service.DashboardService;
import com.example.taskflow.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/statistics")
    public ApiResponse<DashboardStatisticsDto> statistics(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        if (from.isAfter(to)) {
            throw new IllegalArgumentException("'from' must be before 'to'");
        }
        return ApiResponse.createSuccess( "통계 조회 성공",dashboardService.getStatistics(from, to));
    }

    @GetMapping
    public ApiResponse<DashboardStatisticsDto>
}
