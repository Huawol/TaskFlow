package com.example.taskflow.log;


import com.example.taskflow.common.exception.LogNotFoundException;
import com.example.taskflow.log.dto.request.ActivityLogCreateRequestDto;
import com.example.taskflow.log.dto.request.ActivityLogRequestDto;
import com.example.taskflow.log.dto.response.ActivityLogResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

    @Service
    @RequiredArgsConstructor
    @Transactional
    public class ActivityService {

    private final ActivityRepository activityRepository;


    //타입유형별 메세지
    private String newDescription(ActivityLogCreateRequestDto requestDto) {
        return requestDto.getActivityType().description(requestDto);
    }

    //로그저장
    public void save(ActivityLogCreateRequestDto requestDto) {

        //활동 유형에 따라 메세지 생성
        String newedDescription = newDescription(requestDto);
        //DTO->Entity
        ActivityLog log = ActivityLog.create(requestDto,newedDescription);
        activityRepository.save(log);
    }




    //전체조회 + 조건 별 조회
    @Transactional(readOnly = true)
    public Page<ActivityLogResponseDto> searchLogs(@Valid ActivityLogRequestDto requestDto, Pageable pageable) {
        LocalDate startDate = requestDto.getStartDate();
        LocalDate endDate = requestDto.getEndDate();
        ActivityType activityType = requestDto.getActivityType();
        Long userId = requestDto.getUserId();

        Page<ActivityLog> logs;

        // 날짜가 있으면 범위 계산 날짜 입력 시 ->LocalDateTime 변환
        boolean hasDateRange = startDate != null && endDate != null;
        //날짜 범위 조건 있는지 판별 <조건> ? <참 때 값> : <거짓 떄 값>
        LocalDateTime from = hasDateRange ? startDate.atStartOfDay() : null;
        LocalDateTime to = hasDateRange ? endDate.atTime(LocalTime.MAX) : null;

        //조건 별 쿼리 선택
        if (userId != null && activityType != null && hasDateRange){//userId +활동 유형,날짜필터
            logs = activityRepository.findByUserIdAndActivityTypeAndTimestampAndDeletedFalse(userId, activityType, from, to, pageable);
        } else if (userId != null && hasDateRange) { //userId + 날짜
            logs = activityRepository.findByUserIdAndTimestampAndDeletedFalse(userId,from,to,pageable);
        } else if (activityType != null && hasDateRange) { //활동 유형 + 날짜
            logs = activityRepository.findByActivityTypeAndTimestampAndDeletedFalse(activityType,from,to,pageable);
        } else if (userId != null && activityType != null) { //userId + 활동유형
            logs = activityRepository.findByUserIdAndActivityTypeAndDeletedFalse(userId,activityType,pageable);
        } else if (userId != null) { //사용자만
            logs = activityRepository.findByUserIdAndDeletedFalse(userId,pageable);
        } else if (activityType != null) { //활동유형만
            logs = activityRepository.findByActivityTypeAndDeletedFalse(activityType,pageable);
        } else if (hasDateRange) { //날짜만
           logs = activityRepository.findByTimestampBetweenAndDeletedFalse(from,to,pageable);
        } else { //전체 조회
            logs = activityRepository.findByDeletedFalse(pageable);
        }
        return logs.map(ActivityLogResponseDto::toDto);
        }

    //업데이트
    public void updateLog(Long id, @Valid String newDescription) {
        ActivityLog log = activityRepository.findById(id)
                .orElseThrow(() -> new LogNotFoundException("로그가 존재하지 않습니다."));

        log.updateDescription(newDescription);
    }

    //논리삭제
    public void deleteLog(Long id) {
        ActivityLog log = activityRepository.findById(id)
                .orElseThrow(() -> new LogNotFoundException("로그가 존재하지 않습니다."));

        log.softDelete();
    }
}
