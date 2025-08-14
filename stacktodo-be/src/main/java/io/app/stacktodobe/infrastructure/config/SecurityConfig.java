package io.app.stacktodobe.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;

@Configuration
public class SecurityConfig {
    
    @Bean
    Pbkdf2PasswordEncoder passwordEncoder() {
        return Pbkdf2PasswordEncoder.defaultsForSpringSecurity_v5_8();
    }

    // Spring Security 설정을 여기에 추가합니다.
    // 예: 인증 및 인가 관련 설정, CORS 설정 등
    // 예: JWT 토큰 필터, 사용자 인증 서비스 등

    // 현재는 빈 설정으로 남겨두지만, 필요에 따라 보안 관련 설정을 추가할 수 있습니다.

    @Bean
    DefaultSecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/api/v1/members/signup").permitAll()
                        .anyRequest().authenticated()
                )
                .build();

    }
}
