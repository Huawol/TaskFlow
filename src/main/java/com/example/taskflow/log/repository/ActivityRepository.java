package com.example.taskflow.log.repository;

import com.example.taskflow.log.entity.ActivityType;
import com.example.taskflow.log.entity.ActivityLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface ActivityRepository extends JpaRepository<ActivityLog, Long> {
    Page<ActivityLog> findByDeletedFalse(Pageable pageable);

    Page<ActivityLog> findByTimestampBetweenAndDeletedFalse(LocalDateTime start, LocalDateTime end, Pageable pageable);

    //userId + 날짜
    Page<ActivityLog> findByUserIdAndTimestampAndDeletedFalse(Long userId, LocalDateTime start, LocalDateTime end, Pageable pageable);

    //활동 유형 + 날짜
    Page<ActivityLog> findByActivityTypeAndTimestampAndDeletedFalse(ActivityType type, LocalDateTime start, LocalDateTime end, Pageable pageable);

    //userId +활동 유형,날짜필터
    Page<ActivityLog> findByUserIdAndActivityTypeAndTimestampAndDeletedFalse(Long userId, ActivityType type, LocalDateTime start, LocalDateTime end, Pageable pageable);

    //userId + 활동유형
    Page<ActivityLog> findByUserIdAndActivityTypeAndDeletedFalse(Long userId, ActivityType type, Pageable pageable);

    //사용자만
    Page<ActivityLog> findByUserIdAndDeletedFalse(Long userId, Pageable pageable);

    //활동유형만
    Page<ActivityLog> findByActivityTypeAndDeletedFalse(ActivityType type, Pageable pageable);
}
