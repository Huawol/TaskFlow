package com.example.taskflow.user.controller;

import com.example.taskflow.user.dto.request.RegisterRequest;
import com.example.taskflow.user.dto.request.WithdrawRequest;
import com.example.taskflow.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest req) {
        userService.register(req.getUsername(), req.getEmail(), req.getPassword(), req.getName());
        return ResponseEntity.ok("회원가입 완료");
    }

    @PostMapping("/withdraw/{userId}")
    public ResponseEntity<?> withdraw(@PathVariable Long userId, @RequestBody WithdrawRequest req) {
        userService.withdraw(userId, req.getPassword());
        return ResponseEntity.ok("회원탈퇴 완료");
    }
}

