package com.example.taskflow.log;

import com.example.taskflow.common.ApiResponse;
import com.example.taskflow.log.dto.request.LogCreateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/logs")
@RequiredArgsConstructor
public class ActivityLogController {

    private final ActivityService activityService;

//    @PostMapping
//    public ResponseEntity<ApiResponse<LogCreateRequestDto>>
}
