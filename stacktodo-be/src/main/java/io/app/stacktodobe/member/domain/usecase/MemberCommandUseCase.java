package io.app.stacktodobe.member.domain.usecase;


import io.app.stacktodobe.member.adapter.command.MemberCommandExecutor;
import io.app.stacktodobe.member.persistence.repository.MemberRepository;
import io.app.stacktodobe.member.presentation.command.MemberCreateCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberCommandUseCase {

    private final MemberRepository memberRepository;

    public void createMember(MemberCreateCommand command) {
        MemberCommandExecutor executor = new MemberCommandExecutor(memberRepository::save);
        executor.execute(command);
    }
}
