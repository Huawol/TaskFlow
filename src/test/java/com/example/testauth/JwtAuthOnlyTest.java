package com.example.testauth;

import com.example.taskflow.security.config.JwtUtil;
import com.example.taskflow.security.enums.UserRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class JwtAuthOnlyTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtUtil jwtUtil; // 네가 만든 JWT 생성/파싱 유틸

    @Test
    void jwt_토큰이_없으면_401_반환() throws Exception {
        mockMvc.perform(get("/api/protected")) // JWT 인증 필터가 걸려있는 엔드포인트
                .andExpect(status().isUnauthorized());
    }

    @Test
    void 올바른_jwt_토큰이_있으면_200_반환() throws Exception {
        // 1. 토큰 직접 생성
        String token = jwtUtil.createToken(1L, "test@example.com", UserRole.USER);

        // 2. Authorization 헤더에 넣어서 요청
        mockMvc.perform(get("/api/protected")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    void 잘못된_jwt_토큰이면_401_반환() throws Exception {
        mockMvc.perform(get("/api/protected")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + "invalid.token.value"))
                .andExpect(status().isUnauthorized());
    }
}

