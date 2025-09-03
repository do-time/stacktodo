package io.app.stacktodobe.member.domain;

import io.app.stacktodobe.member.adapter.in.web.dto.MemberCreateCommand;
import io.app.stacktodobe.member.adapter.out.persistence.entity.MemberEntity;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@NoArgsConstructor
@Data
@Setter(value = AccessLevel.PRIVATE)
public class Member {

    private String email;
    private UUID memberId;
    private String password;
    private String nickname;
    private String profileImage;
    private String phoneNumber;


    public static Member createMember(MemberCreateCommand command) {
        Member member = new Member();
        member.setMemberId(UUID.randomUUID());
        member.setEmail(command.email());
        member.setPassword(command.password());
        member.setNickname(command.nickname());
        member.setProfileImage(command.profileImage());
        member.setPhoneNumber(command.phoneNumber());
        return member;
    }
}
