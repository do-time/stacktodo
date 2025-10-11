package io.app.stacktodobe.member.application.port.in.command;


public record MemberCreateCommand(
        String email,
        String password,
        String hashedPassword,
        String username,
        String profileImage,
        String phoneNumber
) {
}
