package io.app.stacktodobe.member.persistence.entity;


import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "member",
        uniqueConstraints = @UniqueConstraint(columnNames = {"member_id", "email"}))
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Column(name = "id")
    private Long dataKey;

    @Column(name = "member_id", unique = true, nullable = false)
    private UUID memberId;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    private String hashedPassword;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "profile_image")
    private String profileImage;

    @Column(name = "phone_number")
    private String phoneNumber;


}
