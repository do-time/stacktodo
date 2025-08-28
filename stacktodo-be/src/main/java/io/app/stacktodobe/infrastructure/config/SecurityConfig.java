package io.app.stacktodobe.infrastructure.config;

import io.app.stacktodobe.infrastructure.jwt.JwtKeyHolder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.DefaultSecurityFilterChain;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

@Configuration
@Profile("!test")
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
    DefaultSecurityFilterChain securityFilterChain(HttpSecurity http, JwtDecoder jwtDecoder) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .oauth2ResourceServer(c -> c.jwt(jwt -> jwt.decoder(jwtDecoder)))
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/api/v1/members/signup").permitAll()
                        .requestMatchers("/api/v1/members/issueToken").permitAll()
                        .anyRequest().authenticated()
                )
                .build();
    }

    @Bean
    JwtDecoder jwtDecoder(JwtKeyHolder jwtKeyHolder) {
        return NimbusJwtDecoder.withSecretKey(jwtKeyHolder.secretKey())
                .build();
    }

    @Bean
    JwtKeyHolder jwtKeyHolder(@Value("${security.jwt.secret}") String jwtSecret) {
        SecretKey key = new SecretKeySpec(jwtSecret.getBytes(), "HmacSHA256");
        return new JwtKeyHolder(key);
    }
}
