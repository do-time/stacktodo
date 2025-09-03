package io.app.stacktodobe.member.application.port.in.usecase;

import io.app.stacktodobe.infrastructure.jwt.JwtKeyHolder;
import io.app.stacktodobe.member.exception.InvalidCommandException;
import io.app.stacktodobe.member.adapter.out.persistence.entity.MemberEntity;
import io.app.stacktodobe.member.adapter.out.persistence.repository.MemberRepository;
import io.app.stacktodobe.member.adapter.in.web.dto.AccessTokenCarrier;
import io.app.stacktodobe.member.adapter.in.web.dto.IssueTokenCommand;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IssueTokenCommandUseCase {

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtKeyHolder jwtKeyHolder;

    public AccessTokenCarrier issueToken(IssueTokenCommand command) {
        MemberEntity memberEntity = memberRepository.findByEmail(command.email()).orElseThrow(() -> new IllegalArgumentException("Member not found with email: " + command.email()));

        if (!passwordEncoder.matches(command.password(), memberEntity.getHashedPassword())) {
            throw new InvalidCommandException("Invalid password for email: " + command.email());
        }

        String accessToken = Jwts
                .builder()
                .setSubject(memberEntity.getEmail())
                .signWith(jwtKeyHolder.secretKey())
                .compact();
        return new AccessTokenCarrier(accessToken);
    }


}
