package com.example.taskflow.log.aop;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    @Around("execution(* com.example.taskflow.log.service.ActivityService.*(..))")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {

        String methodName = joinPoint.getSignature().toShortString();
        log.info("시작 {}",methodName);

        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();

        long end = System.currentTimeMillis();
        log.info("종료 {} | 실행시간 {}ms",methodName,end - start);

        return result;

    }
}
