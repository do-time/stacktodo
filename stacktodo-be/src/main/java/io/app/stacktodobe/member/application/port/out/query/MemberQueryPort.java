package io.app.stacktodobe.member.application.port.out.query;

import io.app.stacktodobe.member.adapter.in.web.dto.MemberView;
import io.app.stacktodobe.member.adapter.out.persistence.repository.MemberRepository;

import java.util.UUID;

public interface MemberQueryPort {
    MemberView memberMe(UUID memberId);
}
