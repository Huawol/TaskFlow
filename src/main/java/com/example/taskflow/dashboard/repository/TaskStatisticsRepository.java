package com.example.taskflow.dashboard.repository;

import com.example.taskflow.dashboard.dto.TaskStatusCountDto;
import com.example.taskflow.task.entity.Status;
import com.example.taskflow.task.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TaskStatisticsRepository extends JpaRepository<Task, Long> {

    @Query("""
            SELECT t
            FROM Task t
            WHERE t.status = :status
            """
    )
    List<Task> searchTaskByStatus_Todo(@Param("status") Status status);

    //기간 내 전체 task 개수
    @Query("""
            SELECT COUNT(t)
            FROM Task t
            WHERE t.deleted = false
            AND t.createdAt BETWEEN :from AND :to
            
            """
    )
    long countByCreatedAtBetween(@Param("from") LocalDateTime from,
                                 @Param("to") LocalDateTime to);


    //기간+상태별 task 수

    @Query("""
            SELECT COUNT(t)
            FROM Task t
            WHERE t.status IN :status
            AND t.deleted = false
            AND t.createdAt BETWEEN :from AND :to
            """)
    long countByStatusAndPeriod(@Param("status") Status status,
                                @Param("from") LocalDateTime from,
                                @Param("to") LocalDateTime to);

    //특정 유저의 해당 날짜 마감인 태스크 목록 조회 // task에 assignedTo 쓰는거
    @Query("""
            SELECT t
            FROM Task t
            WHERE t.assignedTo = :userId
            AND DATE(t.deadline) = :date
            AND t.status IN (:statuses)
            AND t.deleted = false
            """)
    List<Task> findAllByUserIdAndDate(@Param("userId") Long userId,
                                      @Param("date") LocalDate date,
                                      @Param("statuses") List<Status> statuses);


    //마감기한이 지난 작업 개수
    @Query("""
                SELECT COUNT(t)
                FROM Task t
                WHERE t.status IN (:statuses)
                  AND t.deadline < :now
                  AND t.deleted = false
            """)
    long countOverdueTasks(@Param("statuses") List<Status> statuses, @Param("now") LocalDateTime now);

//    /*전체 태스크 상태별 개수 통계
//    전체 비율 통계 계산용*/
    @Query("""
                SELECT new com.example.taskflow.dashboard.dto.TaskStatusCountDto(
                    t.status,
                    COUNT(t)
                )
                FROM Task t
                WHERE t.deleted = false
                GROUP BY t.status
            """)
    List<TaskStatusCountDto> countGroupByStatus();

    //특정 유저(로그인 한 사용자)의 상태별 태스크 개수(개인 비율 계산용)
    @Query("""
                     SELECT new com.example.taskflow.dashboard.dto.TaskStatusCountDto(
                                t.status, COUNT(t))
                     FROM Task t
                     WHERE t.assignedTo.id = :userId
                     AND t.deleted = false
                     GROUP BY t.status
            """)
    List<TaskStatusCountDto> countMyStatus(@Param("userId") Long userId);

    @Query("""
            SELECT count(t) FROM Task t WHERE t.deleted = false                        
            """)
    Long countAllTasks();

    @Query("""
            SELECT t FROM Task t WHERE t.deleted = false ORDER BY
            CASE t.priority
            WHEN 'LOW' THEN 1
            WHEN 'MEDIUM' THEN 2
            WHEN 'HIGH' THEN 3
            END DESC
            """)
    List<Task> findTasksByQuery();
}
