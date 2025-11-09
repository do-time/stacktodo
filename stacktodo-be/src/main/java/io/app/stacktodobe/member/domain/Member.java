package io.app.stacktodobe.member.domain;

import io.app.stacktodobe.member.application.port.in.command.OAuthMemberCreateCommand;
import io.app.stacktodobe.member.application.port.in.command.MemberCreateCommand;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

import static java.util.Objects.requireNonNull;

@NoArgsConstructor
@Data
@Setter(value = AccessLevel.PRIVATE)
public class Member {
    private Email email;

    private UUID memberId;

    private Password password;

    @Setter(AccessLevel.PUBLIC)
    private String hashedPassword;

    private String username;

    private String profileImage;

    private String phoneNumber;

    private String provider;

    private String providerId;

    private String role;


    public static Member createMember(MemberCreateCommand command, PasswordEncoder passwordEncoder) {
        Member member = new Member();

        member.setMemberId(requireNonNull(UUID.randomUUID()));

        Email address = new Email(requireNonNull(command.email()));
        member.setEmail(address);

        String encodedPwd = passwordEncoder.encode(command.password());
        member.setPassword(new Password(requireNonNull(command.password())));
        member.setHashedPassword(requireNonNull(encodedPwd));

        member.setUsername(requireNonNull(command.username()));

        member.setProfileImage(command.profileImage());
        member.setPhoneNumber(command.phoneNumber());

        return member;
    }

    public static Member createMemberByOauth(OAuthMemberCreateCommand command) {
        Member member = new Member();

        member.setMemberId(requireNonNull(UUID.randomUUID()));
        member.setEmail(new Email(requireNonNull(command.email())));
        member.setUsername(requireNonNull(command.username()));
        member.setProfileImage(requireNonNull(command.profileImage()));
        member.setPhoneNumber(requireNonNull(command.phoneNumber()));
        member.setProvider(requireNonNull(command.provider()));
        member.setProviderId(requireNonNull(command.providerId()));
        member.setRole("ROLE_USER");

        return member;

    }
}
