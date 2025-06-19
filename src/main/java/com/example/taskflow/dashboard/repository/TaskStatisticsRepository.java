package com.example.taskflow.dashboard.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.taskflow.dashboard.dto.TaskStatusCountDto;
import com.example.taskflow.task.entity.Status;
import com.example.taskflow.task.entity.Task;

@Repository
public interface TaskStatisticsRepository extends JpaRepository<Task, Long> {

	@Query("""
		SELECT t
		FROM Task t
		WHERE t.status = :status
		"""
	)
	List<Task> searchTaskByStatus_Todo(@Param("status") Status status);

	/*
	전체 태스크 상태별 개수 통계
	전체 비율 통계 계산용
	*/
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