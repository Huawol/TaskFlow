package com.example.taskflow.security.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

	// 토큰 서명에 사용되는 비밀키
	private String secretKey;

	// 토큰의 bearer 접두사
	private String bearerPrefix;

}