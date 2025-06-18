package com.example.taskflow.log.controller;

import com.example.taskflow.common.ApiResponse;
import com.example.taskflow.log.entity.ActivityType;
import com.example.taskflow.log.service.ActivityService;
import com.example.taskflow.log.dto.request.ActivityUpdateRequestDto;
import com.example.taskflow.log.dto.request.ActivityLogCreateRequestDto;
import com.example.taskflow.log.dto.request.ActivityLogRequestDto;
import com.example.taskflow.log.dto.response.ActivityLogResponseDto;
import com.example.taskflow.security.dto.AuthUserDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@RestController
@RequestMapping("/activities")
@RequiredArgsConstructor
public class ActivityLogController {

    private final ActivityService activityService;

    //활동 로그 전체 조회 or 조건별 조회
    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public ResponseEntity<ApiResponse<Page<ActivityLogResponseDto>>> readAllLogs(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false, name = "type") ActivityType activityType,
            @RequestParam(required = false, name = "taskId") Long targetId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate endDate,
            @PageableDefault(size = 10, sort = "timestamp", direction = Sort.Direction.DESC) Pageable pageable) {

            LocalDateTime from = (startDate != null) ? startDate.atStartOfDay() : null;
            LocalDateTime to = (endDate != null) ? endDate.atTime(LocalTime.MAX) : null;

        Page<ActivityLogResponseDto> result = activityService.searchLogs(userId, activityType,
                targetId, from, to ,pageable);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(true,"조회가 완료 되었습니다.",result));
    }


}
