package io.app.stacktodobe.member.application.port.out.command;

import io.app.stacktodobe.member.domain.Member;

public interface MemberCreatePort {

    public void createMember(Member member);
}
