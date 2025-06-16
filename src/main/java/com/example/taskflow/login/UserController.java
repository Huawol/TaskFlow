package com.example.taskflow.login;

import com.example.taskflow.common.ApiResponse;
import com.example.taskflow.login.dto.*;
import com.example.taskflow.security.config.JwtUtil;
import com.example.taskflow.security.enums.UserRole;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserServiceImpl userServiceImpl;
    private final JwtUtil jwtUtil;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<SignupResponseDto>> signUp(@Valid @RequestBody SignupRequestDto signupRequestDto) {
        SignupResponseDto signupResponseDto = userServiceImpl.signUp(signupRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "회원 가입되었습니다.", signupResponseDto));
    }

    @GetMapping("/login")
    public ResponseEntity<ApiResponse<TokenResponseDto>> Login(
            @Valid @RequestBody LoginRequestDto loginRequestDto) {
        LoginResponseDto loginResponseDto = userServiceImpl.login(loginRequestDto);

        String token = jwtUtil.createToken(loginResponseDto.getId(), loginResponseDto.getEmail(), UserRole.USER);

        TokenResponseDto tokenResponseDto = new TokenResponseDto(token);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(true, "로그인이 완료되었습니다.", tokenResponseDto));
    }
}
