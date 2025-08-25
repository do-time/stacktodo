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
        return memberRepository.findByEmail(command.email())
                .filter(member -> passwordEncoder.matches(command.password(), member.getHashedPassword()))
                .map(this::composeToken)
                .map(AccessTokenCarrier::new)
                .orElseThrow(() -> new InvalidCommandException("Invalid email or password"));
    }

    private String composeToken(Member member) {
        return Jwts
                .builder()
                .setSubject(member.getMemberId().toString())
                .signWith(jwtKeyHolder.secretKey())
                .compact();
    }

}
