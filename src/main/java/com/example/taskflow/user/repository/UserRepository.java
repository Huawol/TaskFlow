package com.example.taskflow.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.taskflow.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
