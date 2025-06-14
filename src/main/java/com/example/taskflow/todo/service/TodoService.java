package com.example.taskflow.todo.service;

import org.springframework.stereotype.Service;

import com.example.taskflow.todo.repository.TodoRepository;
import com.example.taskflow.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TodoService {
	private final TodoRepository todoRepository;
	private final UserRepository userRepository;
}
