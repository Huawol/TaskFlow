package com.example.taskflow.task.repository;

import java.util.List;

import com.example.taskflow.task.entity.Task;

public interface TaskCustomRepository {

	List<Task> searchTasksByQueryDsl(String username, String content, String status);
}
