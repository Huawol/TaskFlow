package com.example.taskflow.log.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.taskflow.log.aop.LoggingAspect;

@Configuration
public class WebConfig {

	//필터설정
	//AOP 설정
	@Bean
	public LoggingAspect loggingAspect() {
		return new LoggingAspect();
	}

}