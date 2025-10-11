package io.app.stacktodobe.member.application.port.in.usecase;

import io.app.stacktodobe.member.application.port.in.command.MemberCreateCommand;

public interface MemberCommandUseCase {
    public void createMember(MemberCreateCommand command);

}
