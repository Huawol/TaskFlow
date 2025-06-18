package com.example.taskflow.dashboard.repository;

import com.example.taskflow.dashboard.dto.ActivityLogDto;
import com.example.taskflow.log.ActivityLog;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

// 로그 페이지 네이션으로 출력하는거
public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long> {
    @Query("""
            SELECT new com.example.taskflow.dashboard.dto.ActivityLogDto(
                  u.name,
                  al.message,
                  al.createdAt)
             FROM ActivityLog al
            JOIN al.user u
            WHERE al.createdAt BETWEEN :from AND :to
             AND u.deleted = false
            """)
    List<ActivityLogDto> fetchFeed(@Param("from") LocalDateTime from,
                                   @Param("to") LocalDateTime to,
                                   Pageable pageable);
}
