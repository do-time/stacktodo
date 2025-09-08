package io.app.stacktodobe.member.adapter.in.web.dto;

import io.app.stacktodobe.member.application.port.in.command.MemberCreateCommand;

public record CreateMemberDto(
        String email,
        String password,
        String username,
        String profileImage,
        String phoneNumber
) {

    public static MemberCreateCommand of(CreateMemberDto dto) {
        return new MemberCreateCommand(
                dto.email(),
                dto.password(),
                null,
                dto.username(),
                dto.profileImage(),
                dto.phoneNumber()
        );
    }
}
