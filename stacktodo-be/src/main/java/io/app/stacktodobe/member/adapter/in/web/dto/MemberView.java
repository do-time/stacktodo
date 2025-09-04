package io.app.stacktodobe.member.adapter.in.web.dto;

import io.app.stacktodobe.member.adapter.out.persistence.entity.MemberEntity;

import java.util.UUID;

public record MemberView(
        Long id,
        UUID memberId,
        String email,
        String username,
        String profileImage,
        String phoneNumber
) {

    public static MemberView from(MemberEntity entity) {
        return new MemberView(
                entity.getId(),
                entity.getMemberId(),
                entity.getEmail(),
                entity.getUsername(),
                entity.getProfileImage(),
                entity.getPhoneNumber()
        );
    }
}
