package io.app.stacktodobe.member.presentation.command;

public record MemberCreateCommand(
        String email,
        String password,
        String nickname,
        String profileImage,
        String phoneNumber
) {

}
