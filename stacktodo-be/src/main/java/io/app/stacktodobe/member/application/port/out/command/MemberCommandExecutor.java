package io.app.stacktodobe.member.application.port.out.command;

import io.app.stacktodobe.member.domain.Member;
import io.app.stacktodobe.member.exception.InvalidCommandException;
import io.app.stacktodobe.member.adapter.out.persistence.entity.MemberEntity;
import io.app.stacktodobe.member.application.port.in.command.MemberCreateCommand;

import java.util.function.Consumer;

public class MemberCommandExecutor {

    private final Consumer<MemberEntity> saveMember;

    public MemberCommandExecutor(Consumer<MemberEntity> saveMember) {
        this.saveMember = saveMember;
    }

    public void execute(Member member) {
        validate(member);

        saveMember.accept(MemberEntity.domainToEntity(member, member.getHashedPassword()));
    }


    public void validate(Member member) {
        // 이메일 검증, 중복확인
        if (member.getEmail() == null || !member.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new InvalidCommandException("잘못된 이메일 형식입니다");
        }

        // 비밀번호 검증
        String password = member.getPassword();
        if (password == null || password.length() < 8) {
            throw new InvalidCommandException("비밀번호는 8자리 이상이어야 합니다");
        }
        if (!password.matches(".*[A-Za-z].*") || !password.matches(".*\\d.*")) {
            throw new InvalidCommandException("Password must contain letters and numbers.");
        }

    }
}
