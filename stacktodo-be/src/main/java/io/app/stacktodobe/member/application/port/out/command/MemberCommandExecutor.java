package io.app.stacktodobe.member.application.port.out.command;

import io.app.stacktodobe.member.domain.Member;
import io.app.stacktodobe.member.exception.InvalidCommandException;
import io.app.stacktodobe.member.adapter.out.persistence.entity.MemberEntity;
import io.app.stacktodobe.member.adapter.in.web.dto.MemberCreateCommand;

import java.util.function.Consumer;

public class MemberCommandExecutor {

    private final Consumer<MemberEntity> saveMember;

    public MemberCommandExecutor(Consumer<MemberEntity> saveMember) {
        this.saveMember = saveMember;
    }

    public void execute(MemberCreateCommand command, String hashedPassword) {
        validate(command);
        Member member = MemberCreateCommand.of(command);

        saveMember.accept(MemberEntity.domainToEntity(member, hashedPassword));
    }


    public void validate(MemberCreateCommand command) {
        // 이메일 검증, 중복확인
        if (command.email() == null || !command.email().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new InvalidCommandException("잘못된 이메일 형식입니다");
        }

        // 비밀번호 검증
        String password = command.password();
        if (password == null || password.length() < 8) {
            throw new InvalidCommandException("비밀번호는 8자리 이상이어야 합니다");
        }
        if (!password.matches(".*[A-Za-z].*") || !password.matches(".*\\d.*")) {
            throw new InvalidCommandException("Password must contain letters and numbers.");
        }

    }
}
