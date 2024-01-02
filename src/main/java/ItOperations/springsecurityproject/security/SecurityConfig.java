package ItOperations.springsecurityproject.security;

import ItOperations.springsecurityproject.common.ApiUtils;
import ItOperations.springsecurityproject.common.CommonResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

import static ItOperations.springsecurityproject.common.exception.ErrorCode.ACCESS_FORBIDDEN;
import static ItOperations.springsecurityproject.common.exception.ErrorCode.AUTHENTICATION_UNAUTHORIZED;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    private final JwtProvider jwtProvider;
    private final ObjectMapper objectMapper;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic(AbstractHttpConfigurer::disable
                )
                .csrf(AbstractHttpConfigurer::disable
                )
                // h2 접속하려면 필요함
                .headers(headers -> headers
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                // 세션을 생성, 사용하지 않음
                .sessionManagement((sessionManagement) -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        // 회원가입,로그인, 토큰 재발급, h2 콘솔 접속 -> 모두 승인
                        .requestMatchers("/register", "/login", "/refresh").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()
                        // /admin 아래 요청 -> ADMIN 권한 보유 회원만 허용
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        // /user 아래 요청 -> USER 권한이 있는 회원만 허용
                        .requestMatchers("/user/**").hasRole("USER")
                        .anyRequest().permitAll()
                )
                // JWT 인증 필터
                .addFilterBefore(new JwtAuthenticationFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class)
                // 에러 핸들링
                .exceptionHandling(exceptionHandling -> {
                    exceptionHandling.authenticationEntryPoint(new AuthenticationEntryPoint() { // 인증
                        @Override
                        public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
                            // 인증문제 발생 시 401
                            response.setStatus(401);
                            response.setCharacterEncoding("utf-8");
                            response.setContentType("application/json; charset=UTF-8");

                            // Response 형식에 맞게 변형
                            CommonResponse<Integer> responseBody = ApiUtils.fail(AUTHENTICATION_UNAUTHORIZED.getStatus(), AUTHENTICATION_UNAUTHORIZED.getMessage());
                            response.getWriter().write(objectMapper.writeValueAsString(responseBody));
                        }
                    });
                    exceptionHandling.accessDeniedHandler(new AccessDeniedHandler() { //인가
                        @Override
                        public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
                            // 권한 문제 발생 시 403
                            response.setStatus(403);
                            response.setCharacterEncoding("utf-8");response.setContentType("application/json; charset=UTF-8");

                            // Response 형식에 맞게 변형
                            CommonResponse<Integer> responseBody = ApiUtils.fail(ACCESS_FORBIDDEN.getStatus(), ACCESS_FORBIDDEN.getMessage());
                            response.getWriter().write(objectMapper.writeValueAsString(responseBody));
                        }
                    });
                });

        return http.build();
    }

    // password 암호화
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}