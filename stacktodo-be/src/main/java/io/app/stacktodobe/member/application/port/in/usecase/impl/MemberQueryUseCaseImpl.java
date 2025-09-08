package io.app.stacktodobe.member.application.port.in.usecase.impl;

import io.app.stacktodobe.member.adapter.in.web.dto.MemberView;
import io.app.stacktodobe.member.adapter.out.persistence.repository.MemberRepository;
import io.app.stacktodobe.member.application.port.in.usecase.MemberQueryUseCase;
import io.app.stacktodobe.member.application.port.out.query.MemberQueryProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberQueryUseCaseImpl implements MemberQueryUseCase {

    private final MemberRepository memberRepository;

    public MemberView memberMe(UUID memberId) {
        // Implementation to retrieve member information
        MemberQueryProcessor processor = new MemberQueryProcessor();
        return processor.process(memberRepository, memberId);
    }
}
