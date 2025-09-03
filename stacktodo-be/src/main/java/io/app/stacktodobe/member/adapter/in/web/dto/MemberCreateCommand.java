package io.app.stacktodobe.member.adapter.in.web.dto;


import io.app.stacktodobe.member.domain.Member;

public record MemberCreateCommand(
        String email,
        String password,
        String nickname,
        String profileImage,
        String phoneNumber
) {

    public static Member of(MemberCreateCommand command) {
        return Member.createMember(
                command
        );
    }
}
