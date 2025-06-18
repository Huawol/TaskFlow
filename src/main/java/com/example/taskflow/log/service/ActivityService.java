package com.example.taskflow.log.service;


import com.example.taskflow.log.aop.TrackTime;
import com.example.taskflow.log.repository.ActivityRepository;
import com.example.taskflow.log.entity.ActivityType;
import com.example.taskflow.log.dto.response.ActivityLogResponseDto;
import com.example.taskflow.user.entity.User;
import com.example.taskflow.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

    @Service
    @RequiredArgsConstructor
    @Transactional(readOnly = true)
    public class ActivityService {

    private final ActivityRepository activityRepository;
    private final UserRepository userRepository;

    //전체조회 + 조건 별 조회

    @TrackTime
    public Page<ActivityLogResponseDto> searchLogs(Long userId,
                                                   ActivityType activityType,
                                                   Long targetId,
                                                   LocalDateTime startDate,
                                                   LocalDateTime endDate,
                                                   Pageable pageable) {

        return activityRepository.findByAllFilters(
                userId,activityType,targetId,startDate,endDate, pageable)
                .map(log ->{
                       String username = userRepository.findById(log.getUserId())
                .map(User::getUserName)
                .orElse("알 수 없는 계정");
                       return ActivityLogResponseDto.toDto(log,username);
                }
                );

    }



}
