package io.app.stacktodobe.member.adapter.in.web.dto;


public record MemberCreateCommand(
        String email,
        String password,
        String nickname,
        String profileImage,
        String phoneNumber
) {
}
