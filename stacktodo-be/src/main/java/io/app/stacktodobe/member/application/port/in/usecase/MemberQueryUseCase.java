package io.app.stacktodobe.member.application.port.in.usecase;

import io.app.stacktodobe.member.adapter.in.web.dto.MemberView;

import java.util.UUID;

public interface MemberQueryUseCase {
    public MemberView memberMe(UUID memberId);
}
