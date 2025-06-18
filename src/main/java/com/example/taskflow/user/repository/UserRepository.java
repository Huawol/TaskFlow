package com.example.taskflow.user.repository;

import com.example.taskflow.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    //deleted = false 조건으로 User를 조회
    Optional<User> findByEmailAndDeletedFalse(String email);
    boolean existsByEmail(String email);

    Optional<User>findByUserNameAndDeletedFalse(String email);
}