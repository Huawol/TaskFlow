package com.example.taskflow.security;

import java.time.LocalDateTime;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.taskflow.security.Repository.TokenBlacklistRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

//블랙리스트에 등록된 토큰들을 기존 만료시간 기준으로 정시마다 삭제
@Component
@Slf4j
@RequiredArgsConstructor
public class ExpiredTokensRemover {
	private final TokenBlacklistRepository tokenBlacklistRepository;

	@Scheduled(cron = "0 0 * * * *") // 정시마다 실행
	public void removeTokens() {
		int deletedCount = tokenBlacklistRepository.deleteTokenBlacklistByExpiredAtBefore(LocalDateTime.now());

		if (deletedCount > 0) {
			log.info("만료시간이 지난 토큰 {} 개를 삭제했습니다.", deletedCount);
		}
	}
}
