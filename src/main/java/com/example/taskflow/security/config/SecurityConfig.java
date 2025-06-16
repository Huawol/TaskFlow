package com.example.taskflow.security.config;

import com.example.taskflow.security.enums.UserRole;
import com.example.taskflow.security.exception.authentication.JwtAccessDeniedHandler;
import com.example.taskflow.security.exception.authentication.JwtAuthenticationEntryPoint;
import com.example.taskflow.security.filter.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        return httpSecurity
                .cors(Customizer.withDefaults()) //Cross-Origin Resource Sharing // 프론트엔드랑 협업할때 주로 사용
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)

                .sessionManagement(session -> session // session 안쓰겠다고 알리는거
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // config로 url에 대한 인증/인가를 관리
                .authorizeHttpRequests(auth -> auth
                        // todo : 인가할거 추가..
                        // 유저 및 로그인 인가
                        .requestMatchers(HttpMethod.POST, "/api/users").permitAll() // 회원가입
                        .requestMatchers(HttpMethod.GET, "/api/login").permitAll() // 로그인 //http://localhost:8080/login // 인증인가를 확인하지 않고 전부 허용
                        .requestMatchers(HttpMethod.POST, "/api/profiles").hasRole(UserRole.USER.name()) // 프로필 조회

                        // 할일 인가
                        .requestMatchers(HttpMethod.POST, "/api/tasks").hasRole(UserRole.USER.name()) // 생성
                        .requestMatchers(HttpMethod.GET, "/api/tasks/*").hasRole(UserRole.USER.name()) // 단건 조회
                        .requestMatchers(HttpMethod.GET, "/api/tasks").hasRole(UserRole.USER.name()) // 전체 조회
                        .requestMatchers(HttpMethod.PATCH, "/api/tasks/*").hasRole(UserRole.USER.name()) // 수정
                        .requestMatchers(HttpMethod.DELETE, "/api/tasks/*").hasRole(UserRole.USER.name()) // 삭제

                        // 댓글 인가
                        .requestMatchers(HttpMethod.POST, "/api/tasks/comments").hasRole(UserRole.USER.name())
                        .requestMatchers(HttpMethod.GET, "/api/tasks/comments/*").hasRole(UserRole.USER.name())
                        .requestMatchers(HttpMethod.GET, "/api/tasks/comments/*").hasRole(UserRole.USER.name())
                        .requestMatchers(HttpMethod.PATCH, "/api/tasks/comments/*").hasRole(UserRole.USER.name())
                        .requestMatchers(HttpMethod.DELETE, "/api/tasks/comments/*").hasRole(UserRole.USER.name())

                        // 대시보드 인가
                        .requestMatchers(HttpMethod.GET, "/api/dashboards").hasRole(UserRole.USER.name())
                        .requestMatchers(HttpMethod.GET, "/api/dashboards/*").hasRole(UserRole.USER.name())

                        // 로그 조회 인가
                        .requestMatchers(HttpMethod.GET, "/api/logs").hasRole(UserRole.USER.name())

                        .anyRequest().denyAll()
                )

                //필터 등록 // 인증하는  단계를 직접구현해야해서 어렵게 느껴지는거라고 하시는군....
                .addFilterBefore(new JwtFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class) // 토큰 검사
                // 에러 처리까지 완벽하게 하고 싶다면 exceptionHandling 을 등록을 해야한다.
                .exceptionHandling(configure -> configure
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                        .accessDeniedHandler(jwtAccessDeniedHandler)
                )
                .build();
    }
}
