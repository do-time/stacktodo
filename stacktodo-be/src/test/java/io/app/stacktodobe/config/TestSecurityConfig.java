package io.app.stacktodobe.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@TestConfiguration
@Profile("test")  // ★ test 프로필에서만 로드
public class TestSecurityConfig {
    @Bean
    SecurityFilterChain permitAll(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(request -> request.anyRequest().permitAll())
                .build();
    }
}




