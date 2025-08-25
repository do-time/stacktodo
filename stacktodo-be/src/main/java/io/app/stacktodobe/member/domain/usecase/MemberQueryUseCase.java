package io.app.stacktodobe.member.domain.usecase;

import io.app.stacktodobe.member.persistence.repository.MemberRepository;
import io.app.stacktodobe.member.presentation.view.MemberMeView;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberQueryUseCase {

    private final MemberRepository memberRepository;

    public MemberMeView getMemberMe(UUID id) {
        return memberRepository.findMemberByMemberId(id)
                .map(MemberMeView::from)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));
    }
}
