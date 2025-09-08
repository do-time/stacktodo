package io.app.stacktodobe.member.application.port.in.command;


import io.app.stacktodobe.member.domain.Member;

public record MemberCreateCommand(
        String email,
        String password,
        String hashedPassword,
        String username,
        String profileImage,
        String phoneNumber
) {

    public static Member of(MemberCreateCommand command) {
        return Member.createMember(
                command
        );
    }
}
