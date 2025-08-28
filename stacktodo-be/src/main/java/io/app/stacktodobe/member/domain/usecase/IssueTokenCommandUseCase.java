package io.app.stacktodobe.member.domain.usecase;

import io.app.stacktodobe.infrastructure.jwt.JwtKeyHolder;
import io.app.stacktodobe.member.adapter.model.InvalidCommandException;
import io.app.stacktodobe.member.persistence.entity.Member;
import io.app.stacktodobe.member.persistence.repository.MemberRepository;
import io.app.stacktodobe.member.presentation.command.AccessTokenCarrier;
import io.app.stacktodobe.member.presentation.command.IssueTokenCommand;
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
        System.out.println("email = " + command.email());
        Member member = memberRepository.findByEmail(command.email()).orElseThrow(() -> new IllegalArgumentException("Member not found with email: " + command.email()));
        if (!passwordEncoder.matches(command.password(), member.getHashedPassword())) {
            throw new InvalidCommandException("Invalid password for email: " + command.email());
        }
        String accessToken = Jwts
                .builder()
                .setSubject(member.getEmail())
                .signWith(jwtKeyHolder.secretKey())
                .compact();
        return new AccessTokenCarrier(accessToken);
    }


}
