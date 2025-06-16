package com.example.taskflow.user.service;

import com.example.taskflow.security.PasswordEncoder;
import com.example.taskflow.user.entity.User;
import com.example.taskflow.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final Pattern emailPattern = Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    private final Pattern passwordPattern = Pattern.compile("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+=-]).{8,}$");

    @Transactional
    public void register(String username, String email, String password, String name) {
        if (!emailPattern.matcher(email).matches()) {
            throw new IllegalArgumentException("이메일 형식이 올바르지 않습니다.");
        }
        if (!passwordPattern.matcher(password).matches()) {
            throw new IllegalArgumentException("비밀번호 형식이 올바르지 않습니다.");
        }
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        User user = User.builder()
                .username(username)
                .email(email)
                .password(passwordEncoder.encode(password))
                .name(name)
                .role("USER")
                .createdAt(LocalDateTime.now())
                .deleted(false)
                .build();

        userRepository.save(user);
    }

    @Transactional
    public void withdraw(Long userId, String rawPassword) {
        User user = userRepository.findByIdAndDeletedFalse(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        user.setDeleted(true);
        user.setEmail(user.getEmail() + ".deleted." + System.currentTimeMillis());
        user.setModifiedAt(LocalDateTime.now());
    }
}
