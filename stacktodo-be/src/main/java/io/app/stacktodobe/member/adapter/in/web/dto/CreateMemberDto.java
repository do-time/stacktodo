package io.app.stacktodobe.member.adapter.in.web.dto;

public record CreateMemberDto(
        String email,
        String password,
        String nickname,
        String profileImage,
        String phoneNumber
) {

    public static MemberCreateCommand of(CreateMemberDto dto) {
        return new MemberCreateCommand(
                dto.email(),
                dto.password(),
                dto.nickname(),
                dto.profileImage(),
                dto.phoneNumber()
        );
    }
}
