package com.example.taskflow.user.service;


import com.example.taskflow.security.PasswordEncoder;
import com.example.taskflow.user.entity.User;
import com.example.taskflow.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.regex.Pattern;

/**
 * UserService
 * - 회원가입: 유효성 검사 + Bcrypt 암호화 + 저장
 * - 회원탈퇴: 비밀번호 확인 + soft delete + 이메일 변경 (재가입 방지)
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final Pattern emailPattern = Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    private final Pattern passwordPattern = Pattern.compile("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+=-]).{8,}$");

    @Transactional
    public void register(String username, String email, String password, String name) {
        // 이메일 형식 검증
        if (!emailPattern.matcher(email).matches()) {
            throw new IllegalArgumentException("이메일 형식이 올바르지 않습니다.");
        }

        // 비밀번호 형식 검증
        if (!passwordPattern.matcher(password).matches()) {
            throw new IllegalArgumentException("비밀번호 형식이 올바르지 않습니다.");
        }

        // 이메일 중복 체크
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        // 사용자 저장
        User user = User.builder()
                .userName(username)
                .email(email)
                .password(passwordEncoder.encode(password))
                .name(name)
                .role("USER")
                .build();

        userRepository.save(user);
    }

    @Transactional
    public void withdraw(Long userId, String rawPassword) {
        // 사용자 조회 (soft delete되지 않은)
        User user = userRepository.findByIdAndDeletedFalse(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 비밀번호 일치 여부 확인
        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // soft delete 처리 + 이메일 변경
        user.softDelete();
        user.setEmail(user.getEmail() + ".deleted." + System.currentTimeMillis());
    }
}
