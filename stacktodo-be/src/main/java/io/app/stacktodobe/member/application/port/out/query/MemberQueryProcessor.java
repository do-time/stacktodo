package io.app.stacktodobe.member.application.port.out.query;

import io.app.stacktodobe.member.adapter.in.web.dto.MemberView;
import io.app.stacktodobe.member.adapter.out.persistence.entity.MemberEntity;
import io.app.stacktodobe.member.adapter.out.persistence.repository.MemberRepository;
import io.app.stacktodobe.member.domain.Member;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

public class MemberQueryProcessor {


    public MemberView process(MemberRepository memberRepository, UUID memberId) {
        return memberRepository.findByMemberId(memberId)
                .map(MemberView::from)
                .orElseThrow(() -> new IllegalArgumentException("Member not found with id: " + memberId));
    }



}
