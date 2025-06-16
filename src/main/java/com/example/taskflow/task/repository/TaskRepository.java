package com.example.taskflow.task.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.taskflow.task.entity.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {

	Optional<Task> findByIdAndDeletedFalse(Long id);
}
