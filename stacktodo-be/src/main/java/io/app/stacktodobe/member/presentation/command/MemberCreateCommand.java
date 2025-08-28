package io.app.stacktodobe.member.presentation.command;


import io.app.stacktodobe.member.persistence.entity.Member;

public record MemberCreateCommand(
        String email,
        String password,
        String nickname,
        String profileImage,
        String phoneNumber
) {
}
