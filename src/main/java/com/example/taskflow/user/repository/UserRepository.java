package com.example.taskflow.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.taskflow.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

	//deleted = false 조건으로 User를 조회
	Optional<User> findByEmailAndDeletedFalse(String email);

	boolean existsByEmail(String email);

	boolean existsByUserName(String userName);

	Optional<User> findByUserNameAndDeletedFalse(String email);

}