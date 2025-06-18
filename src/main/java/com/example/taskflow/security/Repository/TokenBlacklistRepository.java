package com.example.taskflow.security.Repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.taskflow.security.entity.TokenBlacklist;

public interface TokenBlacklistRepository extends JpaRepository<TokenBlacklist, Long> {
	boolean existsByToken(String token);

	@Query("delete from TokenBlacklist t where t.expiredAt < :expiration")
	int deleteTokenBlacklistByExpiredAtBefore(@Param("expiration") LocalDateTime expiration);
}
