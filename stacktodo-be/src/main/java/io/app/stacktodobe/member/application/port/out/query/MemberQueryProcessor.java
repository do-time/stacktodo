package io.app.stacktodobe.member.application.port.out.query;

import io.app.stacktodobe.member.adapter.in.web.dto.MemberView;
import io.app.stacktodobe.member.adapter.out.persistence.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

public class MemberQueryProcessor {


    public MemberView process(MemberRepository memberRepository, UUID memberId) {
        var member = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found with id: " + memberId));

        return new MemberView(
                member.getId(),
                member.getMemberId(),
                member.getEmail(),
                member.getNickname(),
                member.getProfileImage()
        );
    }



}
