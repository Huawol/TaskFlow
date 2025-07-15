package com.example.taskflow.task.repository;

import java.util.List;

import com.example.taskflow.task.entity.Task;

public interface TaskJpqlRepository {
	List<Task> searchTasksByJpql(String title, String content, String status);
}