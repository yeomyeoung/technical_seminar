package com.example.ZeroTrust.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    // 비밀번호 암호화용 Bean
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Spring Security 필터 체인 설정
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // POST 직접 처리시 CSRF 비활성화
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/home",
                    "/login",
                    "/css/**",
                    "/js/**",
                    "/images/**",
                    "/wooribank_files/**"
                ).permitAll()
                .anyRequest().permitAll() // 🔥 일단 모든 요청을 허용
            );
        // 🔥 .formLogin() 완전히 제거!
        return http.build();
    }
}
