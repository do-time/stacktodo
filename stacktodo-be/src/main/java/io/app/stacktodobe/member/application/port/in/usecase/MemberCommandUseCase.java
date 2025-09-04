package io.app.stacktodobe.member.application.port.in.usecase;


import io.app.stacktodobe.member.application.port.out.command.MemberCommandExecutor;
import io.app.stacktodobe.member.adapter.out.persistence.repository.MemberRepository;
import io.app.stacktodobe.member.application.port.in.command.MemberCreateCommand;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberCommandUseCase {

    private final MemberRepository memberRepository;

    @Transactional
    public void createMember(MemberCreateCommand command, String hashedPassword) {
        MemberCommandExecutor executor = new MemberCommandExecutor(memberRepository::save);

        executor.execute(command, hashedPassword);
    }
}
