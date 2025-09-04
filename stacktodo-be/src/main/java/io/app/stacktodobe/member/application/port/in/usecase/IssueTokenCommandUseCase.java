package io.app.stacktodobe.member.application.port.in.usecase;

import io.app.stacktodobe.infrastructure.jwt.JwtKeyHolder;
import io.app.stacktodobe.member.exception.InvalidCommandException;
import io.app.stacktodobe.member.adapter.out.persistence.entity.MemberEntity;
import io.app.stacktodobe.member.adapter.out.persistence.repository.MemberRepository;
import io.app.stacktodobe.member.adapter.in.web.dto.AccessTokenCarrier;
import io.app.stacktodobe.member.application.port.in.command.IssueTokenCommand;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class IssueTokenCommandUseCase {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtKeyHolder jwtKeyHolder;

    // 이메일 형식 검증을 위한 정규식
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
    );

    public AccessTokenCarrier issueToken(IssueTokenCommand command) {
        // 입력값 검증
        validateInput(command);
        
        // 이메일로 회원 조회
        MemberEntity memberEntity = memberRepository.findByEmail(command.email())
                .orElseThrow(() -> new InvalidCommandException("존재하지 않는 이메일입니다: " + command.email()));

        // 비밀번호 검증
        if (passwordEncoder.matches(command.password(), memberEntity.getHashedPassword()) == false) {
            throw new InvalidCommandException("비밀번호가 일치하지 않습니다");
        }

        // JWT 토큰 생성
        String accessToken = Jwts
                .builder()
                .setSubject(memberEntity.getMemberId().toString())
                .signWith(jwtKeyHolder.secretKey())
                .compact();
                
        return new AccessTokenCarrier(accessToken);
    }

    private void validateInput(IssueTokenCommand command) {
        // 이메일 검증
        if (!StringUtils.hasText(command.email())) {
            throw new InvalidCommandException("이메일은 필수입니다");
        }
        
        if (command.email().length() > 100) {
            throw new InvalidCommandException("이메일은 100자를 초과할 수 없습니다");
        }
        
        if (!EMAIL_PATTERN.matcher(command.email()).matches()) {
            throw new InvalidCommandException("올바른 이메일 형식이 아닙니다");
        }

        // 비밀번호 검증
        if (!StringUtils.hasText(command.password())) {
            throw new InvalidCommandException("비밀번호는 필수입니다");
        }
        
        if (command.password().length() > 100) {
            throw new InvalidCommandException("비밀번호는 100자를 초과할 수 없습니다");
        }
        
        // SQL 인젝션 및 XSS 방지를 위한 특수문자 검증
        if (containsDangerousCharacters(command.email()) || containsDangerousCharacters(command.password())) {
            throw new InvalidCommandException("허용되지 않는 문자가 포함되어 있습니다");
        }
    }

    private boolean containsDangerousCharacters(String input) {
        if (input == null) return false;
        
        // SQL 인젝션 및 XSS 위험 문자들
        String[] dangerousPatterns = {
            "'", "\"", ";", "--", "/*", "*/", "xp_", "sp_", 
            "<script", "</script>", "javascript:", "onload=", "onerror="
        };
        
        String lowerInput = input.toLowerCase();
        for (String pattern : dangerousPatterns) {
            if (lowerInput.contains(pattern)) {
                return true;
            }
        }
        
        return false;
    }
}
