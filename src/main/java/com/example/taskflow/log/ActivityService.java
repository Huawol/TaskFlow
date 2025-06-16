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
import java.time.LocalTime;
import java.util.Optional;

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




    //전체조회
    @Transactional(readOnly = true)
    public Page<ActivityLogResponseDto> findAll(ActivityLogRequestDto requestDto, Pageable pageable) {
        LocalDate startDate = requestDto.getStartDate();
        LocalDate endDate = requestDto.getEndDate();

        Page<ActivityLog> logs;

        if (startDate != null && endDate != null) {
            logs = activityRepository.findByTimestampBetweenAndDeletedFalse(
                    startDate.atStartOfDay(),
                    endDate.atTime(LocalTime.MAX),
            pageable);
             } else {
            logs = activityRepository.findByDeletedFalse(pageable);
        }

        return logs.map(ActivityLogResponseDto::toDto);
    }

    //타입유형별 조회
    @Transactional(readOnly = true)
    public Page<ActivityLogResponseDto> findActivityType(@Valid ActivityLogRequestDto requestDto, Pageable pageable) {
        LocalDate startDate = requestDto.getStartDate();
        LocalDate endDate = requestDto.getEndDate();
        ActivityType activityType = requestDto.getActivityType();

        Page<ActivityLog> logs;

        if (activityType != null && startDate != null && endDate != null) {
            logs = activityRepository.findByActivityTypeAndTimestampAndDeletedFalse(
                    activityType,
                    startDate.atStartOfDay(),
                    endDate.atTime(LocalTime.MAX),
                    pageable);
        } else if (activityType != null) {
            logs = activityRepository.findByActivityTypeAndDeletedFalse(activityType, pageable);
        } else {
           logs = activityRepository.findByDeletedFalse(pageable);
        }
        return logs.map(ActivityLogResponseDto::toDto);
    }
    //유저아이디별 조회
    @Transactional(readOnly = true)
    public Page<ActivityLogResponseDto> findActivityUserId(@Valid ActivityLogRequestDto requestDto, Pageable pageable) {
        LocalDate startDate = requestDto.getStartDate();
        LocalDate endDate = requestDto.getEndDate();
        Long userId = requestDto.getUserId();

        Page<ActivityLog> logs;

        if (userId != null && startDate != null && endDate != null) {
            logs = activityRepository.findByUserIdAndTimestampAndDeletedFalse(
                    userId,
                    startDate.atStartOfDay(),
                    endDate.atTime(LocalTime.MAX),
                    pageable);
        } else if (userId != null) {
            logs = activityRepository.findByUserIdAndDeletedFalse(userId, pageable);
        } else {
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
