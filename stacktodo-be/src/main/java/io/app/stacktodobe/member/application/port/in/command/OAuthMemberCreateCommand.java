package io.app.stacktodobe.member.application.port.in.command;

public record OAuthMemberCreateCommand(
        String email,
        String username,
        String profileImage,
        String phoneNumber,
        String provider,
        String providerId,
        String role
) {
}
