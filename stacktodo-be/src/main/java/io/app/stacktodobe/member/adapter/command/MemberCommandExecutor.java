package io.app.stacktodobe.member.adapter.command;

import io.app.stacktodobe.member.adapter.model.InvalidCommandException;
import io.app.stacktodobe.member.persistence.entity.Member;
import io.app.stacktodobe.member.presentation.command.MemberCreateCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.function.Consumer;

public class MemberCommandExecutor {

    private final Consumer<Member> saveMember;

    public MemberCommandExecutor(Consumer<Member> saveMember) {
        this.saveMember = saveMember;
    }

    public void execute(MemberCreateCommand command, String hashedPassword) {
        validate(command);
        saveMember.accept(Member.createMember(command, hashedPassword));
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
