package io.app.stacktodobe.member.persistence.entity;


import io.app.stacktodobe.member.presentation.command.MemberCreateCommand;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "member",
        uniqueConstraints = @UniqueConstraint(columnNames = {"member_id", "email"}))
//@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@NoArgsConstructor
@Setter(value = AccessLevel.PRIVATE) @Getter
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "member_id", unique = true, nullable = false)
    private UUID memberId;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    private String hashedPassword;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "profile_image")
    private String profileImage;

    @Column(name = "phone_number")
    private String phoneNumber;

    public static Member createMember(MemberCreateCommand command, String hashedPassword) {
        Member member = new Member();
        member.setMemberId(UUID.randomUUID());
        member.setHashedPassword(hashedPassword);
        member.setEmail(command.email());
        member.setUsername(command.nickname());
        member.setProfileImage(command.profileImage());
        member.setPhoneNumber(command.phoneNumber());

        return member;
    }

}
