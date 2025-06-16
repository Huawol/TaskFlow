package com.example.taskflow.log;

import com.example.taskflow.common.ApiResponse;
import com.example.taskflow.log.dto.request.ActivityLogCreateRequestDto;
import com.example.taskflow.log.dto.response.ActivityLogResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/logs")
@RequiredArgsConstructor
public class ActivityLogController {

    private final ActivityService activityService;

//    @PostMapping
//    public ResponseEntity<ApiResponse<String>> create(@Valid @RequestBody ActivityLogCreateRequestDto requestDto) {
//        activityService.save(requestDto);
//        return ResponseEntity.status(HttpStatus.CREATED)
//                .body(new ApiResponse<>(true, "새로운 작업이 생성되었습니다.", null));
//    }

}
