package io.app.stacktodobe.member.persistence.repository;

import io.app.stacktodobe.member.persistence.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
