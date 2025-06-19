package com.example.taskflow.security.config;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import com.example.taskflow.security.enums.UserRole;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "JwtUtil")
@Component
@RequiredArgsConstructor
public class JwtUtil {

	private final JwtProperties jwtProperties;
	private static final long TOKEN_TIME = 60 * 60 * 1000L; // 60분짜리 토큰

	// JWT 서명 알고리즘
	private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

	// 실제 서명에 사용되는 키 객체
	private Key key;

	/**
	 * 빈 초기화 메서드
	 * - 애플리케이션 시작 시 비밀 키를 Base64로 디코딩하여 Key 객체를 초기화
	 */
	@PostConstruct
	public void init() {
		try {
			String secretKey = jwtProperties.getSecretKey();
			if (!StringUtils.hasText(secretKey)) {
				throw new IllegalArgumentException("JWT 시크릿 키가 설정되지 않았습니다.");
			}

			byte[] bytes = Base64.getDecoder().decode(secretKey);
			if (bytes.length < 32) {
				throw new IllegalArgumentException("JWT 시크릿 키는 최소 32바이트 이상이어야 합니다.");
			}

			key = Keys.hmacShaKeyFor(bytes);
		} catch (IllegalArgumentException e) {
			log.error("JWT 시크릿 키 초기화 실패: {}", e.getMessage());
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "JWT 설정 오류");
		}
	}

	/**
	 * JWT 토큰을 생성합니다.
	 *
	 * @param l
	 * @param email 사용자 이름
	 * @param userRole 사용자의 역할 (권한)
	 * @return 생성된 JWT 토큰
	 */
	public String createToken(long l, String email, UserRole userRole) {
		Date date = new Date();
		return jwtProperties.getBearerPrefix() +
			Jwts.builder()
				.setSubject(String.valueOf(l)) // 사용자 식별자 (ID)
				.claim("email", email) // email로 바꿈
				.claim("userRole", userRole) // 사용자 권한 (역할) // 추가
				.setExpiration(new Date(date.getTime() + TOKEN_TIME)) // 만료 시간 설정
				.setIssuedAt(date) // 발급 시간 설정
				.signWith(key, signatureAlgorithm) // 비밀 키와 알고리즘으로 서명
				.compact(); // JWT 토큰 생성
	}

	public String substringToken(String tokenValue) {
		if (!StringUtils.hasText(tokenValue)) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "토큰이 존재하지 않습니다.");
		}
		return tokenValue.substring(7);
	}

	public Claims extractClaims(String token) {
		return Jwts.parserBuilder()
			.setSigningKey(key)
			.build()
			.parseClaimsJws(token)
			.getBody();
	}

	public LocalDateTime getExpiredAt(String token) {
		Claims claims = Jwts.parserBuilder()
			.setSigningKey(key)
			.build()
			.parseClaimsJws(token)
			.getBody();

		return claims.getExpiration().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
	}

	public Long getUserId(String token) {
		return Long.parseLong(extractClaims(token).getSubject());
	}

	public UserRole getUserRole(String token) {
		return UserRole.of(extractClaims(token).get("userRole", String.class));
	}

	public String getEmail(String token) {
		return extractClaims(token).get("email", String.class);
	}

}