package io.app.stacktodobe.member.presentation.view;

import io.app.stacktodobe.member.persistence.entity.Member;

public record MemberMeView(
        String email,
        String nickname,
        String profileImage,
        String phoneNumber

) {
    public static MemberMeView from(Member member) {
        return new MemberMeView(
                member.getEmail(),
                member.getUsername(),
                member.getProfileImage(),
                member.getPhoneNumber()
        );

    }
}
