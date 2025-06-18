package com.example.taskflow.log.repository;

import com.example.taskflow.log.dto.response.ActivityLogResponseDto;
import com.example.taskflow.log.entity.ActivityType;
import com.example.taskflow.log.entity.ActivityLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface ActivityRepository extends JpaRepository<ActivityLog, Long> {

    @Query("""
         SELECT l FROM ActivityLog l
                  WHERE (:userId IS NULL OR l.userId = : userId)
                           AND (:activityType IS NULL OR l.activityType = : activityType)
                                    AND (:targetId IS NULL OR l.targetId = : targetId)
                                             AND (:startDate IS NULL OR l.timestamp >= : startDate)
                                                      AND (:endDate IS NULL OR  l.timestamp <= : endDate)
         """)

    Page<ActivityLog> findByAllFilters(
            @Param("userId") Long userId,
            @Param("activityType") ActivityType activityType,
            @Param("targetId") Long targetId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable);
}
