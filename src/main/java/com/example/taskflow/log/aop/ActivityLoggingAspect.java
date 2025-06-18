package com.example.taskflow.log.aop;


import com.example.taskflow.common.exception.UserNotFoundException;
import com.example.taskflow.log.entity.ActivityLog;
import com.example.taskflow.log.entity.ActivityType;
import com.example.taskflow.log.repository.ActivityRepository;
import com.example.taskflow.security.dto.AuthUserDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Component
@Aspect
public class ActivityLoggingAspect {

    private final ActivityRepository activityRepository;

    @Pointcut("@annotation(com.example.taskflow.log.aop.ActivityLogging)")
    private void activityLoggingPointcut() {}

    @Around("activityLoggingPointcut()")
    public Object logActivity(ProceedingJoinPoint joinPoint) throws Throwable {

        //요청정보 추출
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        // 인증 정보 추출 (User)
         Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Long userId = null;
         if (authentication != null && authentication.getPrincipal() instanceof AuthUserDto user) {
            userId = user.getId();
         }
         if (userId == null) {
         throw new UserNotFoundException("유저를 찾을 수 없습니다.");
         }

            //ActivityType 추출
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();
            ActivityLogging annotation = method.getAnnotation(ActivityLogging.class);
            ActivityType activityType = annotation.value();
            String targetParamName = annotation.targetParam();


        // --- (2) targetId 파라미터 추출 ---
        String[] paramNames = signature.getParameterNames();
        Object[] args = joinPoint.getArgs();

        Long targetId = null;
        for (int i = 0; i < paramNames.length; i++) {
            if (paramNames[i].equals(targetParamName)) {
                Object arg = args[i];
                if (arg instanceof Long) {
                    targetId = (Long) arg;
                } else if (arg instanceof Integer) {
                    targetId = ((Integer) arg).longValue();
                } else if (arg != null) {
                    try {
                        targetId = Long.valueOf(arg.toString());
                    } catch (NumberFormatException e) {
                        log.warn("targetId 파라미터 변환 실패: {}", arg, e);
                    }
                }
                break;
            }
        }
        if (targetId == null) {
            log.warn("targetParam '{}'을(를) 파라미터에서 찾을 수 없습니다.", targetParamName);
        }

        String method = request.getMethod();
        String uri = request.getRequestURI();
        String ipAddress = request.getRemoteAddr();

        Object result = joinPoint.proceed();

        ActivityLog.ActivityLogBuilder log = ActivityLog.builder()
                .userId(userId)
                .activityType(activityType)
                .targetId(targetId)
                .timestamp(LocalDateTime.now())
                .ipAddress(ipAddress)
                .httpMethod(method)
                .url(uri)
                .description(activityType.name()+"활동 로그 AOP")
                .build();

        activityRepository.save(log);
        return result;
    }
}
