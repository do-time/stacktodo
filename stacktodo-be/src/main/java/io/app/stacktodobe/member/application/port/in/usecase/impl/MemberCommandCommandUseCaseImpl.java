package io.app.stacktodobe.member.application.port.in.usecase.impl;


import io.app.stacktodobe.member.adapter.out.persistence.MemberRepositoryAdapter;
import io.app.stacktodobe.member.application.port.in.command.MemberCreateCommand;
import io.app.stacktodobe.member.application.port.in.usecase.MemberCommandUseCase;
import io.app.stacktodobe.member.domain.Member;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberCommandCommandUseCaseImpl implements MemberCommandUseCase {

    private final MemberRepositoryAdapter memberRepositoryAdapter;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void createMember(MemberCreateCommand command) {
        Member member = MemberCreateCommand.of(command);
        //암호 설정 - 단방향 해시 처리
        member.setHashedPassword(passwordEncoder.encode(command.password()));

        memberRepositoryAdapter.createMember(member);
    }
}
