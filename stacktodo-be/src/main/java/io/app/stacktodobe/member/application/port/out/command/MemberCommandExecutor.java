package io.app.stacktodobe.member.application.port.out.command;

import io.app.stacktodobe.member.adapter.out.persistence.entity.MemberEntity;
import io.app.stacktodobe.member.domain.Member;

import java.util.function.Consumer;

public class MemberCommandExecutor {

    private final Consumer<MemberEntity> saveMember;

    public MemberCommandExecutor(Consumer<MemberEntity> saveMember) {
        this.saveMember = saveMember;
    }

    public void execute(Member member) {
        saveMember.accept(MemberEntity.domainToEntity(member, member.getHashedPassword()));
    }
}
