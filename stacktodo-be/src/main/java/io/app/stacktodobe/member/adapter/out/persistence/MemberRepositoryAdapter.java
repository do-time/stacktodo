package io.app.stacktodobe.member.adapter.out.persistence;

import io.app.stacktodobe.member.adapter.in.web.dto.MemberView;
import io.app.stacktodobe.member.adapter.out.persistence.repository.MemberRepository;
import io.app.stacktodobe.member.application.port.out.command.MemberCommandExecutor;
import io.app.stacktodobe.member.application.port.out.command.MemberCreatePort;
import io.app.stacktodobe.member.application.port.out.query.MemberQueryPort;
import io.app.stacktodobe.member.application.port.out.query.MemberQueryProcessor;
import io.app.stacktodobe.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class MemberRepositoryAdapter implements MemberCreatePort, MemberQueryPort {
    private final MemberRepository memberRepository;

    @Override
    public void createMember(Member member) {
        MemberCommandExecutor executor = new MemberCommandExecutor(memberRepository::save);
        executor.execute(member);
    }

    @Override
    public MemberView memberMe(UUID memberId) {

        // Implementation to retrieve member information
        MemberQueryProcessor processor = new MemberQueryProcessor();
        return processor.process(memberRepository, memberId);
    }
}
