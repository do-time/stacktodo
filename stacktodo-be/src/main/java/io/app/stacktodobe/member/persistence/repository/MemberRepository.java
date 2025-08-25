package io.app.stacktodobe.member.persistence.repository;

import io.app.stacktodobe.member.persistence.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);

    Optional<Member> findMemberByMemberId(UUID memberId);
}
