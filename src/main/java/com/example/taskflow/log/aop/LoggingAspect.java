package com.example.taskflow.log.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Aspect
@Component
public class LoggingAspect {

	//실행시간
	@Pointcut("@annotation(com.example.taskflow.log.aop.TrackTime)")
	public void trackTimePointcut() {
	}

	//실행시간 측정 로직
	@Around("trackTimePointcut()")
	public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
		String methodName = joinPoint.getSignature().toShortString();
		log.info("시작 {}", methodName);

		long start = System.currentTimeMillis();
		Object result = joinPoint.proceed();

		long end = System.currentTimeMillis();
		log.info("종료 {} | 실행시간 {}ms", methodName, end - start);

		return result;
	}

}