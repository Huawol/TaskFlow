package com.example.taskflow.user.controller;

import com.example.taskflow.common.ApiResponse;
import com.example.taskflow.security.config.JwtUtil;
import com.example.taskflow.security.dto.AuthUserDto;
import com.example.taskflow.security.enums.UserRole;
import com.example.taskflow.user.dto.LoginRequestDto;
import com.example.taskflow.user.dto.LoginResponseDto;
import com.example.taskflow.user.dto.SignupRequestDto;
import com.example.taskflow.user.dto.SignupResponseDto;
import com.example.taskflow.user.dto.TokenResponseDto;
import com.example.taskflow.user.dto.UserDeleteRequestDto;
import com.example.taskflow.user.service.UserService;
import com.example.taskflow.user.service.UserServiceImpl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
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


    //회원탈퇴 API
    //- 클라이언트가 전달한 비밀번호를 검증 후 soft delete 처리
    //- 인증된 사용자 본인만 가능
    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse<Void>> deleteUser(
        @AuthenticationPrincipal AuthUserDto authUserDto,
        @RequestBody UserDeleteRequestDto requestDto,
        HttpServletRequest request) {
        //request 에서 토큰 값 가져오기
        String token = jwtUtil.substringToken(request.getHeader("Authorization"));

        userService.deleteUser(token, authUserDto.getEmail(), requestDto.getPassword());
        return ResponseEntity.status(HttpStatus.OK)
            .body(new ApiResponse<>(true, "정삭적으로 회원탈퇴가 완료되었습니다.", null));
    }

    //로그아웃 API
    //- 서버 처리 없음, 클라이언트가 토큰을 삭제
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(HttpServletRequest request) {
        //request 에서 토큰 값 가져오기
        String token = jwtUtil.substringToken(request.getHeader("Authorization"));
        userService.logout(token);
        return ResponseEntity.status(HttpStatus.OK)
            .body(new ApiResponse<>(true, "로그아웃 되었습니다. 해당 토큰은 사용할 수 없습니다.", null));
    }
}