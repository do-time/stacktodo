package io.app.stacktodobe.member.adapter.command;

import io.app.stacktodobe.member.adapter.model.InvalidCommandException;
import io.app.stacktodobe.member.persistence.entity.Member;
import io.app.stacktodobe.member.presentation.command.MemberCreateCommand;

import java.util.function.Consumer;

public class MemberCommandExecutor {

    private final Consumer<Member> saveMember;

    public MemberCommandExecutor(Consumer<Member> saveMember) {
        this.saveMember = saveMember;
    }

    public void execute(MemberCreateCommand command) {
        validate(command);
        saveMember.accept(Member.createMember(command));
    }


    public void validate(MemberCreateCommand command) {
        // 이메일 검증, 중복확인
        if (command.email() == null || !command.email().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new InvalidCommandException("잘못된 이메일 형식입니다");
        }
//        if (emailExists(command.email())) {
//            throw new InvalidCommandException("Email already exists.");
//        }

        // 비밀번호 검증
        String password = command.password();
        if (password == null || password.length() < 8) {
            throw new InvalidCommandException("비밀번호는 8자리 이상이어야 합니다");
        }
        if (!password.matches(".*[A-Za-z].*") || !password.matches(".*\\d.*")) {
            throw new InvalidCommandException("Password must contain letters and numbers.");
        }

        // 닉네임검증
        String nickname = command.nickname();
        if (nickname == null || nickname.length() < 2 || nickname.length() > 20) {
            throw new InvalidCommandException("Nickname must be 2-20 characters.");
        }
        if (!nickname.matches("^[A-Za-z0-9가-힣]+$")) {
            throw new InvalidCommandException("Nickname can only contain letters, numbers, and Korean characters.");
        }

    }
}
