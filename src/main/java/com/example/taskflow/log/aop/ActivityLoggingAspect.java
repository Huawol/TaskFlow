package com.example.taskflow.log.aop;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

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

import com.example.taskflow.log.dto.request.ActivityLogCreateRequestDto;
import com.example.taskflow.log.entity.ActivityLog;
import com.example.taskflow.log.entity.ActivityType;
import com.example.taskflow.log.repository.ActivityRepository;
import com.example.taskflow.security.dto.AuthLogUserDto;
import com.example.taskflow.security.dto.AuthUserDto;
import com.example.taskflow.task.dto.request.TaskStatusRequestDto;
import com.example.taskflow.task.entity.Task;
import com.example.taskflow.task.repository.TaskRepository;
import com.example.taskflow.user.dto.LoginResponseDto;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
@Aspect
public class ActivityLoggingAspect {

	private final ActivityRepository activityRepository;
	private final TaskRepository taskRepository;

	/**
	 * @ActivityLogging
	 * 범위 지정
	 */
	@Pointcut("@annotation(com.example.taskflow.log.aop.ActivityLogging)")
	private void activityLoggingPointcut() {
	}

	//Advice 실제 로직
	@Around("activityLoggingPointcut()")
	public Object logActivity(ProceedingJoinPoint joinPoint) throws Throwable {

		//요청정보 추출(ip,url 등 기록용)
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();

		//ActivityType 추출 AOP 프록시 -> MethodSignature 정보
		MethodSignature signature = (MethodSignature)joinPoint.getSignature();
		Method method = signature.getMethod();

		//실행되는 메서드에 어노테이션 가져오기
		ActivityLogging annotation = method.getAnnotation(ActivityLogging.class);
		//enum 가져오기 ex) TASK_CREATED, TASK_UPDATED
		ActivityType activityType = annotation.value();
		//파라미터 값 가져오기 ex)"createdById"
		String targetParamName = annotation.targetParam();

		//targetId 추출 분리
		Long targetId = extractTargetId(signature, joinPoint.getArgs(), targetParamName);

		//TASK_STATUS_CHANGED는 before/after 직접 조회
		ActivityLogCreateRequestDto requestDto = null;

		//requestDto 추출 분리
		if (activityType == ActivityType.TASK_STATUS_CHANGED) {
			String before = "알 수 없음";
			String after = "알 수 없음";

			// 파라미터에서 after 값 추출
			for (Object arg : joinPoint.getArgs()) {
				if (arg instanceof TaskStatusRequestDto dto) {
					after = dto.getStatus();
				}
			}
			// 실행 전에 DB에서 before 값 조회
			if (targetId != null && targetId > 0) {
				Optional<Task> taskOpt = taskRepository.findByIdAndDeletedFalse(targetId);
				if (taskOpt.isPresent()) {
					before = taskOpt.get().getStatus().name();
				}
			}
			requestDto = new ActivityLogCreateRequestDto(
				activityType,
				targetId,
				before,
				after
			);
		} else {
			// 2. 그 외의 경우 DTO 파라미터가 있는지 확인(확장성을 위한 추출)
			requestDto = extractRequestDto(joinPoint.getArgs());
		}

		String methodType = request.getMethod();
		String uri = request.getRequestURI();
		String ipAddress = request.getRemoteAddr();

		/**
		 * 로그인때만 사용되는 로직
		 */
		if (activityType == ActivityType.USER_LOGGED_IN) {
			Object result = joinPoint.proceed();
			// proceed() 이후에 User 정보 또는 LoginResponseDto에서 userId 꺼내기
			Long userId = null;
			if (result instanceof LoginResponseDto responseDto) {
				userId = responseDto.getId();
			}
			if (userId == null) {
				log.warn("[AOP] 로그인 후 userId가 null입니다. 로그 미저장");
				return result;
			}
			String description = activityType.description(null);

			ActivityLog log = ActivityLog.builder()
				.userId(userId)
				.activityType(activityType)
				.targetId(targetId)
				.timestamp(LocalDateTime.now())
				.ipAddress(ipAddress)
				.httpMethod(methodType)
				.url(uri)
				.description(description)
				.build();

			activityRepository.save(log);
			return result;
		}

		//로그아웃시에 사용
		if (activityType == ActivityType.USER_LOGGED_OUT) {
			Object result = joinPoint.proceed();
			// 로그아웃전까지 SecurityContext에 사용자 정보가 있음
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			Long userId = null;

			if (authentication != null) {
				Object principal = authentication.getPrincipal();
				if (principal instanceof AuthLogUserDto user) {
					userId = user.getId();
				} else if (principal instanceof AuthUserDto user) {
					userId = user.getId();
				}
			}

			if (userId == null) {
				log.warn("[AOP] 로그아웃 userId가 null입니다. 로그 미저장");
				return result;
			}
			String description = activityType.description(null);

			ActivityLog log = ActivityLog.builder()
				.userId(userId)
				.activityType(activityType)
				.targetId(0L) // 로그아웃은 엔티티 X
				.timestamp(LocalDateTime.now())
				.ipAddress(request.getRemoteAddr())
				.httpMethod(request.getMethod())
				.url(request.getRequestURI())
				.description(description)
				.build();
			activityRepository.save(log);
			return result;
		}

		//나머지 케이스 실행
		Object result = joinPoint.proceed();

		// 인증 정보 추출 (User) ->Spring Security에서 현재 로그인한 사용자의 인증정보 추출
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Long userId = null;

		if (authentication != null) {
			Object principal = authentication.getPrincipal();

			if (principal instanceof AuthLogUserDto user) {
				userId = user.getId();
			} else if (principal instanceof AuthUserDto user) {
				userId = user.getId();
			}
		}

		if (userId == null) {
			log.warn("[AOP] userId -> null, 로그 저장 x : principal = {}",
				authentication != null ? authentication.getPrincipal() : null);
			return result;
		}

		//description 동적 생성
		String description = activityType.description(requestDto);

		ActivityLog log = ActivityLog.builder()
			.userId(userId)
			.activityType(activityType)
			.targetId(targetId)
			.timestamp(LocalDateTime.now())
			.ipAddress(ipAddress)
			.httpMethod(methodType)
			.url(uri)
			.description(description)
			.build();

		activityRepository.save(log);
		return result;
	}

	//분리 targetId 추출 메서드
	private Long extractTargetId(MethodSignature signature, Object[] args, String targetParamName) {
		String[] paramNames = signature.getParameterNames();
		for (int i = 0; i < paramNames.length; i++) {
			if (paramNames[i].equals(targetParamName)) {
				Object arg = args[i];

				if (arg instanceof Long)
					return (Long)arg;

				if (arg instanceof String s) {
					try {
						return Long.valueOf(s);
					} catch (NumberFormatException ignore) {
					}
				}
				if (arg != null) {
					String[] tryFields = {"id", "targetId", "userName"};
					for (String fieldName : tryFields) {
						try {
							java.lang.reflect.Field field = arg.getClass().getDeclaredField(fieldName);
							field.setAccessible(true);
							Object value = field.get(arg);
							if (value instanceof Long)
								return (Long)value;
							if (value instanceof String s2) {
								try {
									return Long.valueOf(s2);
								} catch (NumberFormatException ignore) {
								}
							}
						} catch (Exception ignore) {
						}
					}
				}
			}
		}

		log.warn("[AOP] targetId를 찾지 못했습니다. targetParamName={}, paramNames={}", targetParamName,
			Arrays.toString(paramNames));

		return 0L;
	}

	// [추가/분리] requestDto 추출 메서드
	private ActivityLogCreateRequestDto extractRequestDto(Object[] args) {
		for (Object arg : args) {
			if (arg instanceof ActivityLogCreateRequestDto dto) {
				return dto;
			}
		}
		return null;
	}

}