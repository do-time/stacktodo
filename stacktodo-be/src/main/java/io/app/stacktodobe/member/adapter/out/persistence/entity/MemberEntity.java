package io.app.stacktodobe.member.adapter.out.persistence.entity;


import io.app.stacktodobe.common.entity.BaseEntity;
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
public class MemberEntity extends BaseEntity {

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

    public static MemberEntity domainToEntity(Member domain, String hashedPassword) {
        MemberEntity memberEntity = new MemberEntity();
        System.out.println("domain :" + domain.toString());
        memberEntity.setMemberId(UUID.randomUUID());
        memberEntity.setHashedPassword(hashedPassword);
        memberEntity.setEmail(domain.getEmail());
        memberEntity.setUsername(domain.getUsername());
        memberEntity.setProfileImage(domain.getProfileImage());
        memberEntity.setPhoneNumber(domain.getPhoneNumber());

        return memberEntity;
    }

}
