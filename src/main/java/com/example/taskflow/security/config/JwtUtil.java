package com.example.taskflow.security.config;

import com.example.taskflow.security.enums.UserRole;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.rmi.ServerException;
import java.security.Key;
import java.util.Base64;
import java.util.Date;


@Slf4j(topic = "JwtUtil")
@Component
public class JwtUtil {

    private static final String BEARER_PREFIX = "Bearer ";
    private static final long TOKEN_TIME = 60 * 60 * 1000L; // 60분짜리 토큰
    // JWT 서명 알고리즘
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    // 애플리케이션 설정 파일에서 주입받은 비밀 키
    @Value("${jwt.secret.key}")
    private String secretKey;
    // 실제 서명에 사용되는 키 객체
    private Key key;

    /**
     * 빈 초기화 메서드
     * - 애플리케이션 시작 시 비밀 키를 Base64로 디코딩하여 Key 객체를 초기화
     */
    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }


    /**
     * JWT 토큰을 생성합니다.
     * @param username 사용자 이름
     * @param userRole 사용자의 역할 (권한)
     * @return 생성된 JWT 토큰
     */
    public String createToken(String username, UserRole userRole) {
        Date date = new Date();

        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(username) // 사용자 식별자 (ID)
                        .claim("auth", userRole) // 사용자 권한 (역할) // 추가
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
