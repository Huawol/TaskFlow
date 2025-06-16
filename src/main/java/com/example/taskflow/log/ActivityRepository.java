package com.example.taskflow.log;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface ActivityRepository extends JpaRepository<ActivityLog, Long> {
    Page<ActivityLog> findByDeletedFalse(Pageable pageable);

    Page<ActivityLog> findByTimestampBetweenAndDeletedFalse(LocalDateTime start, LocalDateTime end, Pageable pageable);

    Page<ActivityLog> findByActivityTypeAndTimestampAndDeletedFalse(ActivityType activityType,LocalDateTime localDateTime, LocalDateTime localDateTime1, Pageable pageable);

    Page<ActivityLog> findByUserIdAndTimestampAndDeletedFalse(Long UserId, LocalDateTime localDateTime, LocalDateTime localDateTime1, Pageable pageable);

    Page<ActivityLog> findByActivityTypeAndDeletedFalse(ActivityType activityType,Pageable pageable);

    Page<ActivityLog> findByUserIdAndDeletedFalse(Long userId, Pageable pageable);
}
