package com.example.taskflow;

import com.example.taskflow.security.config.JwtProperties;
import com.example.taskflow.security.config.JwtUtil;
import com.example.taskflow.security.dto.AuthUserDto;
import com.example.taskflow.security.enums.UserRole;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TaskFlowApplicationTests {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private JwtProperties jwtProperties;

	@Autowired
	private JwtUtil jwtUtil; // 네가 만든 JWT 생성/파싱 유틸

	@Test
	void jwt_토큰이_없으면_401_반환() throws Exception {
		mockMvc.perform(get("/api/protected")) // JWT 인증 필터가 걸려있는 엔드포인트
				.andExpect(status().isUnauthorized());
	}

	@Test
	void createTokenTest(){
		//given
		AuthUserDto userDto = new AuthUserDto(1L, "test@test.com", UserRole.USER);

		//when
		String token = jwtUtil.createToken(userDto.getId(), userDto.getEmail(), userDto.getUserRole());

		//then
		assertNotNull(token);
	}

	@Test
	void 잘못된_jwt_토큰이면_401_반환() throws Exception {
		mockMvc.perform(get("/api/protected")
						.header(HttpHeaders.AUTHORIZATION, "Bearer " + "invalid.token.value"))
				.andExpect(status().isUnauthorized());
	}

	@Test
	@DisplayName("JWT 토큰 파싱 테스트")
	void parseTokenTest(){
		//given
		AuthUserDto userDto = new AuthUserDto(1L, "test@test.com", UserRole.USER);
		String token = jwtUtil.createToken(userDto.getId(), userDto.getEmail(), userDto.getUserRole()); // 이거 바꿔야함...

		//when
		token = jwtUtil.substringToken(token);
		Claims claims = jwtUtil.extractClaims(token);

		//then
		assertNotNull(token);
		assertEquals(String.valueOf(userDto.getId()), claims.getSubject());
		assertEquals(userDto.getEmail(), claims.get("email", String.class));
		assertEquals(userDto.getUserRole().name(), claims.get("userRole", String.class));
	}

	@Test
	@DisplayName("Bearer 변환 테스트")
	void substringTokenTest(){
		//given
		AuthUserDto userDto = new AuthUserDto(1L, "test@test.com", UserRole.USER);
		String token = jwtUtil.createToken(userDto.getId(), userDto.getEmail(), userDto.getUserRole());

		//when
		String substringToken = jwtUtil.substringToken(token);

		//then
		assertEquals(token.replace(jwtProperties.getBearerPrefix(), ""), substringToken);
	}

	@Test
	@DisplayName("JWT 토큰 정보 추출 테스트")
	void getTokenTest(){
		//given
		AuthUserDto userDto = new AuthUserDto(1L, "test@test.com", UserRole.USER);
		String token = jwtUtil.createToken(userDto.getId(), userDto.getEmail(), userDto.getUserRole());

		//when
		token = jwtUtil.substringToken(token);
		Long extractedUserId = jwtUtil.getUserId(token);
		String extractedUserEmail = jwtUtil.getEmail(token);
		UserRole extractedUserRole = jwtUtil.getUserRole(token);

		//then
		assertEquals(userDto.getId(), extractedUserId);
		assertEquals(userDto.getEmail(), extractedUserEmail);
		assertEquals(userDto.getUserRole(), extractedUserRole);
	}

}
