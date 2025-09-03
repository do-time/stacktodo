package io.app.stacktodobe.member.adapter.out.persistence.entity;


import io.app.stacktodobe.member.adapter.in.web.dto.MemberCreateCommand;
import io.app.stacktodobe.member.domain.Member;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "members",
        uniqueConstraints = @UniqueConstraint(columnNames = {"member_id", "email"}))
@Setter(value = AccessLevel.PRIVATE)
@Getter
//@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@NoArgsConstructor
public class MemberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "member_id", unique = true, nullable = false)
    private UUID memberId;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    private String hashedPassword;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "profile_image")
    private String profileImage;

    @Column(name = "phone_number")
    private String phoneNumber;

    public static MemberEntity of(Member domain, String hashedPassword) {
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setMemberId(UUID.randomUUID());
        memberEntity.setHashedPassword(hashedPassword);
        memberEntity.setEmail(domain.getEmail());
        memberEntity.setNickname(domain.getNickname());
        memberEntity.setProfileImage(domain.getProfileImage());
        memberEntity.setPhoneNumber(domain.getPhoneNumber());

        return memberEntity;
    }

}
