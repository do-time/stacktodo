package io.app.stacktodobe.member.domain;

import io.app.stacktodobe.member.application.port.in.command.OAuthMemberCreateCommand;
import io.app.stacktodobe.member.application.port.in.command.MemberCreateCommand;
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
    @Setter(AccessLevel.PUBLIC)
    private String hashedPassword;
    private String username;
    private String profileImage;
    private String phoneNumber;
    private String provider;
    private String providerId;
    private String role;



    public static Member createMember(MemberCreateCommand command) {
        Member member = new Member();
        member.setMemberId(UUID.randomUUID());
        member.setEmail(command.email());
        member.setPassword(command.password());
        member.setUsername(command.username());
        member.setProfileImage(command.profileImage());
        member.setPhoneNumber(command.phoneNumber());
        return member;
    }

    public static Member createMemberByOauth(OAuthMemberCreateCommand command) {
        Member member = new Member();
        member.setMemberId(UUID.randomUUID());
        member.setEmail(command.email());
        member.setUsername(command.username());
        member.setProfileImage(command.profileImage());
        member.setPhoneNumber(command.phoneNumber());
        member.setProvider(command.provider());
        member.setProviderId(command.providerId());
        member.setRole("ROLE_USER");
        return member;

    }
}
