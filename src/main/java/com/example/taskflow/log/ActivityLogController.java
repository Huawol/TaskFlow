package com.example.taskflow.log;

import com.example.taskflow.common.ApiResponse;
import com.example.taskflow.log.dto.ActivityUpdateRequestDto;
import com.example.taskflow.log.dto.request.ActivityLogCreateRequestDto;
import com.example.taskflow.log.dto.request.ActivityLogRequestDto;
import com.example.taskflow.log.dto.response.ActivityLogResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/logs")
@RequiredArgsConstructor
public class ActivityLogController {

    private final ActivityService activityService;


    //활동 로그 생성
    @PostMapping
    public ResponseEntity<ApiResponse<String>> create(@Valid @RequestBody ActivityLogCreateRequestDto requestDto) {
        activityService.save(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "새로운 작업이 생성되었습니다.", null));
    }

    //활동 로그 전체 조회 or 조건별 조회
    @GetMapping
    public ResponseEntity<ApiResponse<Page<ActivityLogResponseDto>>> readAllLogs(
            @ModelAttribute ActivityLogRequestDto requestDto,
            @PageableDefault(size = 10,sort = "timestamp",direction = Sort.Direction.DESC) Pageable pageable)  {
        Page<ActivityLogResponseDto> result = activityService.searchLogs(requestDto, pageable);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(true,"조회가 완료 되었습니다.",result));
    }

    @PatchMapping("/{id}")
    public  ResponseEntity<ApiResponse<String>> update(@PathVariable Long id,
                                                       @Valid@RequestBody ActivityUpdateRequestDto requestDto) {
        activityService.updateLog(id, requestDto.getDescription());
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(true,"수정되었습니다.",null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable Long id) {
        activityService.deleteLog(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(true,"삭제되었습니다.",null));
    }

}
