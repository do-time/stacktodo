package io.app.stacktodobe.member.domain;

import io.app.stacktodobe.member.exception.InvalidCommandException;

public record Password(String value) {

    public Password {

        if (value == null || value.length() < 8) {
            throw new InvalidCommandException("비밀번호는 8자리 이상이어야 합니다");
        }

//        if (!value.matches(".*[A-Za-z].*") || !value.matches(".*\\d.*")) {
//            throw new InvalidCommandException("Password must contain letters and numbers.");
//        }
    }

}
