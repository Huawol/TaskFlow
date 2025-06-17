package com.example.taskflow.login;

import com.example.taskflow.common.SignUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LoginRepository extends JpaRepository<SignUser, Long> {

    boolean existsByEmail(String email);

    Optional<SignUser>findByUserNameAndDeletedFalse(String email);
}
