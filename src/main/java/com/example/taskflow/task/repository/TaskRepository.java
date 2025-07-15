package com.example.taskflow.task.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.taskflow.task.entity.Task;

public interface TaskRepository extends JpaRepository<Task, Long>, TaskCustomRepository, TaskJpqlRepository {

	Optional<Task> findByIdAndDeletedFalse(Long id);

	//검색조건 생성일자 기준 start ~ end
	Page<Task> findByCreatedAtBetweenAndDeletedFalse(LocalDateTime periodStart, LocalDateTime periodEnd,
		Pageable pageable);

	//검색조건 없을시 전체 조회 페이징 리스트 반환
	Page<Task> findAllByDeletedFalse(Pageable pageable);

	//담당자Id로 조회하는 메서드
	List<Task> findByAssignedTo_IdAndDeletedFalse(Long userId);

	//TaskJpqlRepository 메서드
	@Override
	List<Task> searchTasksByJpql(String title, String content, String status);

	//QueryDsl 메서드
	@Override
	List<Task> searchTasksByQueryDsl(String username, String content, String status);

}